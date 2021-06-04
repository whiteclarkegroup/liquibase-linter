(window.webpackJsonp=window.webpackJsonp||[]).push([[19],{112:function(e,n,t){"use strict";t.d(n,"a",(function(){return p})),t.d(n,"b",(function(){return d}));var a=t(0),r=t.n(a);function i(e,n,t){return n in e?Object.defineProperty(e,n,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[n]=t,e}function o(e,n){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);n&&(a=a.filter((function(n){return Object.getOwnPropertyDescriptor(e,n).enumerable}))),t.push.apply(t,a)}return t}function c(e){for(var n=1;n<arguments.length;n++){var t=null!=arguments[n]?arguments[n]:{};n%2?o(Object(t),!0).forEach((function(n){i(e,n,t[n])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):o(Object(t)).forEach((function(n){Object.defineProperty(e,n,Object.getOwnPropertyDescriptor(t,n))}))}return e}function l(e,n){if(null==e)return{};var t,a,r=function(e,n){if(null==e)return{};var t,a,r={},i=Object.keys(e);for(a=0;a<i.length;a++)t=i[a],n.indexOf(t)>=0||(r[t]=e[t]);return r}(e,n);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(a=0;a<i.length;a++)t=i[a],n.indexOf(t)>=0||Object.prototype.propertyIsEnumerable.call(e,t)&&(r[t]=e[t])}return r}var s=r.a.createContext({}),u=function(e){var n=r.a.useContext(s),t=n;return e&&(t="function"==typeof e?e(n):c(c({},n),e)),t},p=function(e){var n=u(e.components);return r.a.createElement(s.Provider,{value:n},e.children)},b={inlineCode:"code",wrapper:function(e){var n=e.children;return r.a.createElement(r.a.Fragment,{},n)}},m=r.a.forwardRef((function(e,n){var t=e.components,a=e.mdxType,i=e.originalType,o=e.parentName,s=l(e,["components","mdxType","originalType","parentName"]),p=u(t),m=a,d=p["".concat(o,".").concat(m)]||p[m]||b[m]||i;return t?r.a.createElement(d,c(c({ref:n},s),{},{components:t})):r.a.createElement(d,c({ref:n},s))}));function d(e,n){var t=arguments,a=n&&n.mdxType;if("string"==typeof e||a){var i=t.length,o=new Array(i);o[0]=m;var c={};for(var l in n)hasOwnProperty.call(n,l)&&(c[l]=n[l]);c.originalType=e,c.mdxType="string"==typeof e?e:a,o[1]=c;for(var s=2;s<i;s++)o[s]=t[s];return r.a.createElement.apply(null,o)}return r.a.createElement.apply(null,t)}m.displayName="MDXCreateElement"},77:function(e,n,t){"use strict";t.r(n),t.d(n,"frontMatter",(function(){return o})),t.d(n,"metadata",(function(){return c})),t.d(n,"rightToc",(function(){return l})),t.d(n,"default",(function(){return u}));var a=t(2),r=t(6),i=(t(0),t(112)),o={title:"create-index-name"},c={unversionedId:"rules/create-index-name",id:"rules/create-index-name",isDocsHomePage:!1,title:"create-index-name",description:"Why?",source:"@site/docs/rules/create-index-name.md",slug:"/rules/create-index-name",permalink:"/liquibase-linter/docs/rules/create-index-name",version:"current",sidebar:"docs",previous:{title:"create-column-remarks",permalink:"/liquibase-linter/docs/rules/create-column-remarks"},next:{title:"create-table-remarks",permalink:"/liquibase-linter/docs/rules/create-table-remarks"}},l=[{value:"Why?",id:"why",children:[]},{value:"Options",id:"options",children:[]},{value:"Example Usage",id:"example-usage",children:[]}],s={rightToc:l};function u(e){var n=e.components,t=Object(r.a)(e,["components"]);return Object(i.b)("wrapper",Object(a.a)({},s,t,{components:n,mdxType:"MDXLayout"}),Object(i.b)("h2",{id:"why"},"Why?"),Object(i.b)("p",null,"When ",Object(i.b)("a",Object(a.a)({parentName:"p"},{href:"http://www.liquibase.org/documentation/changes/create_index.html"}),"creating an index"),", it's possible to omit a specific ",Object(i.b)("inlineCode",{parentName:"p"},"constraintName")," for the index. This is hazardous, because the database vendor will automatically name it for you in an unpredictable way, which makes things difficult later if you want to reference or remove it."),Object(i.b)("p",null,"Also, you might already have a broad standard for object names - and be enforcing it with ",Object(i.b)("a",Object(a.a)({parentName:"p"},{href:"/liquibase-linter/docs/rules/object-name"}),"the object-name rule")," - but you might also want a more specific rule concerning how indexes are named."),Object(i.b)("p",null,"This rule will fail if there is no ",Object(i.b)("inlineCode",{parentName:"p"},"constraintName")," given when creating an index, or when configured with a pattern, will fail if the given name does not match the pattern."),Object(i.b)("h2",{id:"options"},"Options"),Object(i.b)("ul",null,Object(i.b)("li",{parentName:"ul"},Object(i.b)("inlineCode",{parentName:"li"},"pattern")," - (regex, as string) optional regular expression that the name of any created index must adhere to"),Object(i.b)("li",{parentName:"ul"},Object(i.b)("inlineCode",{parentName:"li"},"dynamicValue")," - (string) Spring EL expression, with the ",Object(i.b)("a",Object(a.a)({parentName:"li"},{href:"https://github.com/liquibase/liquibase/blob/master/liquibase-core/src/main/java/liquibase/change/core/CreateIndexChange.java"}),Object(i.b)("inlineCode",{parentName:"a"},"CreateIndexChange"))," instance as its expression scope, that should resolve to a string, and can then be interpolated in the pattern with ",Object(i.b)("inlineCode",{parentName:"li"},"{{value}}"))),Object(i.b)("h2",{id:"example-usage"},"Example Usage"),Object(i.b)("p",null,"To simply ensure that a name is always given:"),Object(i.b)("pre",null,Object(i.b)("code",Object(a.a)({parentName:"pre"},{className:"language-json"}),'{\n    "rules": {\n        "create-index-name": true\n    }\n}\n')),Object(i.b)("p",null,"To ensure that a pattern is matched, including the table name:"),Object(i.b)("pre",null,Object(i.b)("code",Object(a.a)({parentName:"pre"},{className:"language-json"}),'{\n    "rules": {\n        "create-index-name": {\n            "pattern": "^{{value}}_I\\\\d$",\n            "dynamicValue": "tableName",\n            "errorMessage": "Index names must be the table name, suffixed with \'I\' and a number, e.g. FOO_I2"\n        }\n    }\n}\n')))}u.isMDXComponent=!0}}]);