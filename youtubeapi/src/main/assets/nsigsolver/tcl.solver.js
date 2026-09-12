/*!
 * TCL player n-function solver.
 */
var tclSolver = (function (meriyah, astring) {
  'use strict';

  // ---- minimal generic ESTree walker ----

  function forEachChild(node, fn) {
    if (!node || typeof node !== "object") return;
    for (const key in node) {
      if (key === "start" || key === "end" || key === "loc" || key === "range" || key === "type") continue;
      const value = node[key];
      if (Array.isArray(value)) {
        for (const item of value) {
          if (item && typeof item.type === "string") fn(item);
        }
      } else if (value && typeof value.type === "string") {
        fn(value);
      }
    }
  }

  function walkAncestor(node, visitors) {
    function visit(n, ancestors) {
      const fn = visitors[n.type];
      if (fn) fn(n, ancestors);
      const nextAncestors = ancestors.concat([n]);
      forEachChild(n, (child) => visit(child, nextAncestors));
    }
    visit(node, []);
  }

  function findGlobalArray(parsedBaseJs) {
    let globalArrayName = "";
    let globalArrayData = null;
    let globalArrayFound = false;
    let globalArrayBody = parsedBaseJs.body[1]?.expression.callee.body.body || parsedBaseJs.body[0]?.expression.callee.object.body.body;
    for (let node of globalArrayBody) {
      if (globalArrayFound) break;
      if (node?.type === "VariableDeclaration") {
        for (let v of node.declarations) {
          if (v.init?.callee?.object?.type === "Literal") {
            globalArrayName = v.id.name;
            let splitArg = v.init?.arguments[0].value;
            globalArrayData = v.init?.callee?.object.value.split(splitArg.toString());
            globalArrayFound = true;
            break;
          }
          if (v?.init?.elements) {
            globalArrayName = v.id.name;
            let globalArrayDataValue = astring.generate(v.init);
            try {
              globalArrayData = JSON.parse(globalArrayDataValue);
            } catch (e) {
              globalArrayData = Function(`"use strict"; return ${globalArrayDataValue}`)();
            }
            globalArrayFound = true;
            break;
          }
        }
      }
    }
    return { globalArrayName, globalArrayData };
  }

  function findTopLevelLiteralIndexAssignments(body, name) {
    const results = [];
    for (const node of body) {
      if (node?.type !== "ExpressionStatement") continue;
      const expr = node.expression;
      if (expr?.type !== "AssignmentExpression" || expr.operator !== "=") continue;
      const left = expr.left;
      if (
        left?.type === "MemberExpression" &&
        left.computed &&
        left.object?.type === "Identifier" &&
        left.object.name === name &&
        left.property?.type === "Literal" &&
        expr.right?.type === "Literal"
      ) {
        results.push(node);
      }
    }
    return results;
  }

  function findAssigningStatement(body, funcNode) {
    function assignsTarget(node) {
      if (!node || typeof node !== "object") return false;
      if (node.type === "AssignmentExpression" && node.left?.type === "Identifier" && node.left.name === funcNode) {
        return true;
      }
      if (node.type.includes("Function")) return false;
      for (const key in node) {
        if (key === "start" || key === "end" || key === "loc" || key === "range") continue;
        const value = node[key];
        if (Array.isArray(value)) {
          for (const item of value) {
            if (item && typeof item.type === "string" && assignsTarget(item)) return true;
          }
        } else if (value && typeof value.type === "string") {
          if (assignsTarget(value)) return true;
        }
      }
      return false;
    }

    for (const node of body) {
      if (node && assignsTarget(node)) return node;
    }
    return null;
  }

  function findMethodByName(parsed, funcNode) {
    if (!funcNode) return null;
    const dotIndex = funcNode.indexOf(".");
    let body = parsed.body[1]?.expression.callee.body.body || parsed.body[0]?.expression.callee.object.body.body;
    if (dotIndex !== -1) {
      const objName = funcNode.substring(0, dotIndex);
      const propName = funcNode.substring(dotIndex + 1);
      for (let node of body) {
        if (node?.type === "ExpressionStatement") {
          const expr = node.expression;
          if (
            expr?.type === "AssignmentExpression" &&
            expr.left?.type === "MemberExpression" &&
            !expr.left.computed &&
            expr.left.object?.name === objName &&
            expr.left.property?.name === propName &&
            expr.right
          ) {
            return node;
          }
        }
      }
      return null;
    }
    let bareDeclarationFound = false;
    for (let node of body) {
      if (node?.type === "FunctionDeclaration" && node.id?.name === funcNode) {
        return node;
      }
      if (node?.type === "VariableDeclaration") {
        for (let v of node.declarations) {
          if (v.id.name === funcNode) {
            if (!v.init) bareDeclarationFound = true;
            if (v.init) {
              const wasEmptyArrayLiteral = v.init.type === "ArrayExpression" && v.init.elements.length === 0;
              if (v.init.type === "ArrayExpression" && v.init.elements.every(el => el?.type === "Identifier")) {
                for (let i = 0; i < v.init.elements.length; i++) {
                  v.init = findMethodByName(parsed, v.init.elements[i].name);
                }
              }
              const declNode = {
                type: "VariableDeclaration",
                kind: node.kind,
                declarations: [v],
                start: node.start,
                end: node.end,
              };

              if (wasEmptyArrayLiteral) {
                const indexAssignments = findTopLevelLiteralIndexAssignments(body, funcNode);
                if (indexAssignments.length) {
                  return {
                    type: "Program",
                    sourceType: "script",
                    body: [declNode, ...indexAssignments],
                    start: node.start,
                    end: indexAssignments[indexAssignments.length - 1].end,
                  };
                }
              }
              return declNode;
            }
          }
        }
      }
      if (node?.type === "ExpressionStatement") {
        if (node?.expression?.left?.name === funcNode) {
          if (node?.expression?.right) return node;
        }
      }
      if (node?.type === "AssignmentExpression" && node.left.name === funcNode) {
        if (node.right) return node;
      }
    }

    const assigningStatement = findAssigningStatement(body, funcNode);
    if (assigningStatement) {
      return {
        type: "Program",
        sourceType: "script",
        body: [
          {
            type: "VariableDeclaration",
            kind: "var",
            declarations: [{ type: "VariableDeclarator", id: { type: "Identifier", name: funcNode }, init: null }],
          },
          assigningStatement,
        ],
        start: assigningStatement.start,
        end: assigningStatement.end,
      };
    }

    if (bareDeclarationFound) {
      return {
        type: "VariableDeclaration",
        kind: "var",
        declarations: [{ type: "VariableDeclarator", id: { type: "Identifier", name: funcNode }, init: null }],
      };
    }

    return null;
  }

  function ensureVarDeclaration(node, code) {
    if (
      node.type === "ExpressionStatement" &&
      node.expression?.type === "AssignmentExpression" &&
      node.expression.left?.type === "Identifier"
    ) {
      return "var " + code;
    }
    return code;
  }

  function collectPrototypeMethods(parsed, constructorName, globalArrayData, globalArrayName) {
    const results = [];
    const body = parsed.body[1]?.expression.callee.body.body || parsed.body[0]?.expression.callee.object.body.body;
    const ctorNorm = constructorName.trim();
    const aliasMap = new Map();

    function protoTargetOf(node) {
      if (!node || node.type !== "MemberExpression") return null;
      if (!node.computed) {
        if (node.property?.name === "prototype" || node.property?.value === "prototype") {
          return astring.generate(node.object).trim();
        }
        return null;
      }
      if (globalArrayData && globalArrayName) {
        const protoExpr = node.property;
        if (
          protoExpr?.type === "MemberExpression" &&
          protoExpr.object?.name === globalArrayName &&
          protoExpr.property?.type === "Literal" &&
          globalArrayData[protoExpr.property.value] === "prototype"
        ) {
          return astring.generate(node.object).trim();
        }
      }
      return null;
    }

    for (const node of body) {
      if (node.type === "VariableDeclaration") {
        for (const decl of node.declarations) {
          if (decl.id?.type === "Identifier" && decl.init) {
            const target = protoTargetOf(decl.init);
            if (target) aliasMap.set(decl.id.name, target);
          }
        }
      }
      if (
        node.type === "ExpressionStatement" &&
        node.expression?.type === "AssignmentExpression" &&
        node.expression.left?.type === "Identifier"
      ) {
        const target = protoTargetOf(node.expression.right);
        if (target) aliasMap.set(node.expression.left.name, target);
      }

      if (node.type !== "ExpressionStatement") continue;
      const expr = node.expression;
      if (expr?.type !== "AssignmentExpression") continue;
      const left = expr.left;
      if (left.type !== "MemberExpression") continue;

      if (!left.computed && left.object?.type === "Identifier") {
        const aliasTarget = aliasMap.get(left.object.name);
        if (aliasTarget && aliasTarget === ctorNorm) {
          const rewritten = JSON.parse(JSON.stringify(node));
          rewritten.expression.left.object = meriyah.parse(ctorNorm + ".prototype").body[0].expression;
          results.push(rewritten);
          continue;
        }
      }

      const obj = left.object;
      if (obj.type !== "MemberExpression") continue;

      if (left.computed && obj.computed && globalArrayData && globalArrayName) {
        const protoExpr = obj.property;
        if (
          protoExpr?.type === "MemberExpression" &&
          protoExpr.object?.name === globalArrayName &&
          protoExpr.property?.type === "Literal"
        ) {
          const protoIdx = protoExpr.property.value;
          if (globalArrayData[protoIdx] === "prototype") {
            const ctorGenerated = astring.generate(obj.object).trim();
            if (ctorGenerated === ctorNorm) {
              results.push(node);
              continue;
            }
          }
        }
      }

      if (!obj.computed && (obj.property?.name === "prototype" || obj.property?.value === "prototype")) {
        const ctorGenerated = astring.generate(obj.object).trim();
        if (ctorGenerated === ctorNorm) {
          results.push(node);
        }
      }
    }

    return results;
  }

  const excludeArrayMethodsName = [
    "await","break","case","catch","class","const","continue","debugger","default",
    "delete","do","else","enum","export","extends","false","finally","for","function",
    "if","implements","import","in","instanceof","interface","let","new","null","package",
    "private","protected","public","return","static","super","switch","this","throw",
    "true","try","typeof","var","void","while","with","yield",
    "eval","isFinite","isNaN","parseFloat","parseInt","decodeURI","decodeURIComponent",
    "encodeURI","encodeURIComponent",
    "Object","Function","Boolean","Symbol","Error","EvalError","InternalError","RangeError",
    "ReferenceError","SyntaxError","TypeError","URIError","Number","BigInt","Math",
    "Date","String","RegExp","Array","Map","Set","WeakMap","WeakSet","ArrayBuffer",
    "SharedArrayBuffer","DataView","JSON","Promise","Generator","GeneratorFunction",
    "AsyncFunction","Reflect","Proxy","Intl","globalThis",
    "Buffer","process","global","__dirname","__filename","module","exports","require"
  ];

  function getUndeclaredMethods(funcCode, nFunctionName) {
    let declared = new Set();
    let unDeclared = new Set();

    function addDeclaredFromParams(params) {
      for (const p of params) {
        if (p.type === "Identifier") declared.add(p.name);
      }
    }

    walkAncestor(funcCode, {
      VariableDeclarator(node) {
        declared.add(node.id.name);
      },
      FunctionDeclaration(node) {
        declared.add(node.id.name);
        addDeclaredFromParams(node.params);
      },
      FunctionExpression(node) {
        addDeclaredFromParams(node.params);
      },
      ArrowFunctionExpression(node) {
        addDeclaredFromParams(node.params);
      },
      MemberExpression(node) {
        if (!node.computed && node.object.type === "Identifier") {
          const fullName = node.object.name + "." + node.property.name;
          if (node.object.name !== nFunctionName) unDeclared.add(fullName);
        }
      },
      Identifier(node, ancestors) {
        if (!ancestors || ancestors.length === 0) return;
        const parent = ancestors[ancestors.length - 1];
        if (node.name === nFunctionName) return;
        if (parent.type === "MemberExpression" && parent.property === node && !parent.computed) return;
        if (parent.type === "VariableDeclarator" && parent.id === node) return;
        if ((parent.type === "FunctionDeclaration" || parent.type === "FunctionExpression") && parent.id === node) return;
        if (parent.type === "Property" && parent.key === node && !parent.computed && !parent.shorthand) return;
        if (parent.type === "LabeledStatement" && parent.label === node) return;
        if (parent.type === "BreakStatement" || parent.type === "ContinueStatement") return;
        unDeclared.add(node.name);
      },
    });
    unDeclared = new Set([...unDeclared].filter(name => !excludeArrayMethodsName.includes(name)));
    declared = new Set([...declared].filter(name => !excludeArrayMethodsName.includes(name)));

    return new Set([...unDeclared].filter(name => !declared.has(name)));
  }

  function collectDependencies(entryCode, challengeName, parsedBaseJs) {
    const processed = new Set();
    const codeMap = new Map();
    const depsMap = new Map();
    const { globalArrayName, globalArrayData } = findGlobalArray(parsedBaseJs);

    function collect(name) {
      if (name === challengeName || processed.has(name)) return;
      processed.add(name);

      const node = findMethodByName(parsedBaseJs, name);
      if (!node) return;

      const rawCode = astring.generate(node).replace("};;", "};").replace("};\n;", "};\n");
      const code = ensureVarDeclaration(node, rawCode);
      codeMap.set(name, code);

      const parsed = meriyah.parse(code);
      const deps = new Set(getUndeclaredMethods(parsed, challengeName));

      const protoMethods = collectPrototypeMethods(parsedBaseJs, name, globalArrayData, globalArrayName);
      if (protoMethods.length) {
        const protoKey = name + "$$proto";
        const protoRawCode = protoMethods.map(n => {
          const raw = astring.generate(n).replace("};;", "};").replace("};\n;", "};\n");
          return ensureVarDeclaration(n, raw);
        }).join("\n");
        codeMap.set(protoKey, protoRawCode);

        const protoParsed = meriyah.parse(protoRawCode);
        const protoDeps = getUndeclaredMethods(protoParsed, challengeName);
        for (const dep of protoDeps) deps.add(dep);
        depsMap.set(protoKey, new Set([...protoDeps, name]));
      }

      depsMap.set(name, deps);

      for (const dep of deps) collect(dep);
    }

    const initialDeps = getUndeclaredMethods(meriyah.parse(entryCode), challengeName);
    for (const name of initialDeps) collect(name);

    const sorted = [];
    const visited = new Set();

    function topoSort(name) {
      if (visited.has(name)) return;
      visited.add(name);
      for (const dep of (depsMap.get(name) || [])) topoSort(dep);
      if (codeMap.has(name)) sorted.push(name);
    }

    for (const name of codeMap.keys()) topoSort(name);

    return sorted.map(name => codeMap.get(name)).join("\n");
  }

  function preprocessPlayer(baseJsData) {
    let parsedBaseJs = meriyah.parse(baseJsData);
    let func = /\]\]&&\([A-z0-9$]+\=([A-z0-9$]+)\(([0-9]+),([0-9]+),[A-z0-9$]+\)\);(?:\)|)/.exec(baseJsData);
    if (!func) throw new Error("Could not locate TCL n-function call site");

    let funcArgs = func[2] + "," + func[3];
    let funcName = func[1];

    let globalArray = findGlobalArray(parsedBaseJs);
    let startFunc = findMethodByName(parsedBaseJs, funcName);
    if (!startFunc) throw new Error("Could not locate n-function body for " + funcName);
    let additionalCode = collectDependencies(astring.generate(startFunc), funcName, parsedBaseJs);

    let firstPoint = /;var ([A-z0-9$]+)=new ([A-z0-9$]+)\([A-z0-9]+,[A-z0-9]+\);\1/.exec(baseJsData);
    if (!firstPoint) throw new Error("Could not locate URL-builder constructor");
    let gph = firstPoint[2];

    let r = "var g = {}; \n var " + globalArray.globalArrayName + "=" + JSON.stringify(globalArray.globalArrayData) + ";\n" + astring.generate(startFunc) + "\n" + additionalCode + "\n";

    const ctorNode = findMethodByName(parsedBaseJs, gph);
    const ctorCode = ctorNode
      ? ensureVarDeclaration(ctorNode, astring.generate(ctorNode).replace("};;", "};").replace("};\n;", "};\n"))
      : "";

    const protoMethodsCode = collectPrototypeMethods(
      parsedBaseJs, gph, globalArray.globalArrayData, globalArray.globalArrayName
    ).map(n => {
      const rawCode = astring.generate(n).replace("};;", "};").replace("};\n;", "};\n");
      return ensureVarDeclaration(n, rawCode);
    }).join("\n");

    const gphDeps = collectDependencies(ctorCode + "\n" + protoMethodsCode, gph, parsedBaseJs);

    let gphProtoMethods = gphDeps + "\n" + ctorCode + "\n" + protoMethodsCode;

    let prePart = "let newObjectWithUrlObject = new " + gph + "(nValue, true); \n\n";
    return (
      "if (typeof globalThis.XMLHttpRequest === 'undefined') { globalThis.XMLHttpRequest = { prototype: {} }; }\n" +
      "if (typeof location !== 'undefined') { try { location.href = 'https://www.youtube.com/watch?v=yt-dlp-wins'; } catch (e) {} }\n" +
      r + gphProtoMethods +
      "\nvar nFunction=function(nValue) { " + prePart + "; " + funcName + "(" + funcArgs + ", newObjectWithUrlObject); return newObjectWithUrlObject['get']('n') };" +
      "\nreturn nFunction;"
    );
  }

  // Entry point matching the calling convention used by TclChallengeProvider:
  // input = { type: "player", player: "<base.js source>", challenges: [...], output_preprocessed: true }
  //       | { type: "preprocessed", preprocessed_player: "<cached script from a prior result>", challenges: [...] }
  // output = { type: "result", results: { "<challenge>": "<decoded n>" }, preprocessed_player?: "..." }
  //        | { type: "error", error: "..." }
  function main(input) {
    try {
      const preprocessedPlayer = input.type === "preprocessed"
        ? input.preprocessed_player
        : preprocessPlayer(input.player);
      const nFunction = new Function(preprocessedPlayer)();
      const results = {};
      for (const challenge of input.challenges) {
        results[challenge] = nFunction(challenge);
      }
      const output = { type: "result", results: results };
      if (input.type === "player" && input.output_preprocessed) {
        output.preprocessed_player = preprocessedPlayer;
      }
      return output;
    } catch (error) {
      return {
        type: "error",
        error: error instanceof Error ? `${error.message}\n${error.stack}` : `${error}`,
      };
    }
  }

  return main;
})(meriyah, astring);
