(window.webpackJsonp=window.webpackJsonp||[]).push([[51],{106:function(e,n,t){"use strict";t.r(n),t.d(n,"frontMatter",(function(){return i})),t.d(n,"metadata",(function(){return c})),t.d(n,"rightToc",(function(){return l})),t.d(n,"default",(function(){return u}));var r=t(2),a=t(6),o=(t(0),t(112)),i={title:"no-raw-sql"},c={unversionedId:"rules/no-raw-sql",id:"rules/no-raw-sql",isDocsHomePage:!1,title:"no-raw-sql",description:"Why?",source:"@site/docs/rules/no-raw-sql.md",slug:"/rules/no-raw-sql",permalink:"/liquibase-linter/docs/rules/no-raw-sql",version:"current",sidebar:"docs",previous:{title:"no-schema-name",permalink:"/liquibase-linter/docs/rules/no-schema-name"},next:{title:"object-name",permalink:"/liquibase-linter/docs/rules/object-name"}},l=[{value:"Why?",id:"why",children:[]},{value:"Options",id:"options",children:[]},{value:"Example Usage",id:"example-usage",children:[]}],s={rightToc:l};function u(e){var n=e.components,t=Object(a.a)(e,["components"]);return Object(o.b)("wrapper",Object(r.a)({},s,t,{components:n,mdxType:"MDXLayout"}),Object(o.b)("h2",{id:"why"},"Why?"),Object(o.b)("p",null,"Liquibase supports ",Object(o.b)("a",Object(r.a)({parentName:"p"},{href:"http://www.liquibase.org/documentation/changes/index.html"}),"a lot of different changes"),", and raw sql change types are only useful for edge cases or complex cases.\nBy default changes should be done using Liquibase\u2019s automated refactoring tags and not raw sql."),Object(o.b)("p",null,"This rule will fail if either ",Object(o.b)("inlineCode",{parentName:"p"},"<sql>")," or ",Object(o.b)("inlineCode",{parentName:"p"},"<sqlFile>")," change types are used."),Object(o.b)("h2",{id:"options"},"Options"),Object(o.b)("p",null,"No extra options."),Object(o.b)("h2",{id:"example-usage"},"Example Usage"),Object(o.b)("pre",null,Object(o.b)("code",Object(r.a)({parentName:"pre"},{className:"language-json"}),'{\n    "rules": {\n        "no-raw-sql": true\n    }\n}\n')))}u.isMDXComponent=!0},112:function(e,n,t){"use strict";t.d(n,"a",(function(){return p})),t.d(n,"b",(function(){return f}));var r=t(0),a=t.n(r);function o(e,n,t){return n in e?Object.defineProperty(e,n,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[n]=t,e}function i(e,n){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);n&&(r=r.filter((function(n){return Object.getOwnPropertyDescriptor(e,n).enumerable}))),t.push.apply(t,r)}return t}function c(e){for(var n=1;n<arguments.length;n++){var t=null!=arguments[n]?arguments[n]:{};n%2?i(Object(t),!0).forEach((function(n){o(e,n,t[n])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):i(Object(t)).forEach((function(n){Object.defineProperty(e,n,Object.getOwnPropertyDescriptor(t,n))}))}return e}function l(e,n){if(null==e)return{};var t,r,a=function(e,n){if(null==e)return{};var t,r,a={},o=Object.keys(e);for(r=0;r<o.length;r++)t=o[r],n.indexOf(t)>=0||(a[t]=e[t]);return a}(e,n);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(r=0;r<o.length;r++)t=o[r],n.indexOf(t)>=0||Object.prototype.propertyIsEnumerable.call(e,t)&&(a[t]=e[t])}return a}var s=a.a.createContext({}),u=function(e){var n=a.a.useContext(s),t=n;return e&&(t="function"==typeof e?e(n):c(c({},n),e)),t},p=function(e){var n=u(e.components);return a.a.createElement(s.Provider,{value:n},e.children)},b={inlineCode:"code",wrapper:function(e){var n=e.children;return a.a.createElement(a.a.Fragment,{},n)}},d=a.a.forwardRef((function(e,n){var t=e.components,r=e.mdxType,o=e.originalType,i=e.parentName,s=l(e,["components","mdxType","originalType","parentName"]),p=u(t),d=r,f=p["".concat(i,".").concat(d)]||p[d]||b[d]||o;return t?a.a.createElement(f,c(c({ref:n},s),{},{components:t})):a.a.createElement(f,c({ref:n},s))}));function f(e,n){var t=arguments,r=n&&n.mdxType;if("string"==typeof e||r){var o=t.length,i=new Array(o);i[0]=d;var c={};for(var l in n)hasOwnProperty.call(n,l)&&(c[l]=n[l]);c.originalType=e,c.mdxType="string"==typeof e?e:r,i[1]=c;for(var s=2;s<o;s++)i[s]=t[s];return a.a.createElement.apply(null,i)}return a.a.createElement.apply(null,t)}d.displayName="MDXCreateElement"}}]);