(window.webpackJsonp=window.webpackJsonp||[]).push([[11],{112:function(e,n,t){"use strict";t.d(n,"a",(function(){return p})),t.d(n,"b",(function(){return f}));var r=t(0),a=t.n(r);function o(e,n,t){return n in e?Object.defineProperty(e,n,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[n]=t,e}function c(e,n){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);n&&(r=r.filter((function(n){return Object.getOwnPropertyDescriptor(e,n).enumerable}))),t.push.apply(t,r)}return t}function i(e){for(var n=1;n<arguments.length;n++){var t=null!=arguments[n]?arguments[n]:{};n%2?c(Object(t),!0).forEach((function(n){o(e,n,t[n])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):c(Object(t)).forEach((function(n){Object.defineProperty(e,n,Object.getOwnPropertyDescriptor(t,n))}))}return e}function s(e,n){if(null==e)return{};var t,r,a=function(e,n){if(null==e)return{};var t,r,a={},o=Object.keys(e);for(r=0;r<o.length;r++)t=o[r],n.indexOf(t)>=0||(a[t]=e[t]);return a}(e,n);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(r=0;r<o.length;r++)t=o[r],n.indexOf(t)>=0||Object.prototype.propertyIsEnumerable.call(e,t)&&(a[t]=e[t])}return a}var l=a.a.createContext({}),u=function(e){var n=a.a.useContext(l),t=n;return e&&(t="function"==typeof e?e(n):i(i({},n),e)),t},p=function(e){var n=u(e.components);return a.a.createElement(l.Provider,{value:n},e.children)},m={inlineCode:"code",wrapper:function(e){var n=e.children;return a.a.createElement(a.a.Fragment,{},n)}},d=a.a.forwardRef((function(e,n){var t=e.components,r=e.mdxType,o=e.originalType,c=e.parentName,l=s(e,["components","mdxType","originalType","parentName"]),p=u(t),d=r,f=p["".concat(c,".").concat(d)]||p[d]||m[d]||o;return t?a.a.createElement(f,i(i({ref:n},l),{},{components:t})):a.a.createElement(f,i({ref:n},l))}));function f(e,n){var t=arguments,r=n&&n.mdxType;if("string"==typeof e||r){var o=t.length,c=new Array(o);c[0]=d;var i={};for(var s in n)hasOwnProperty.call(n,s)&&(i[s]=n[s]);i.originalType=e,i.mdxType="string"==typeof e?e:r,c[1]=i;for(var l=2;l<o;l++)c[l]=t[l];return a.a.createElement.apply(null,c)}return a.a.createElement.apply(null,t)}d.displayName="MDXCreateElement"},69:function(e,n,t){"use strict";t.r(n),t.d(n,"frontMatter",(function(){return c})),t.d(n,"metadata",(function(){return i})),t.d(n,"rightToc",(function(){return s})),t.d(n,"default",(function(){return u}));var r=t(2),a=t(6),o=(t(0),t(112)),c={title:"no-schema-name"},i={unversionedId:"rules/no-schema-name",id:"rules/no-schema-name",isDocsHomePage:!1,title:"no-schema-name",description:"Why?",source:"@site/docs/rules/no-schema-name.md",slug:"/rules/no-schema-name",permalink:"/liquibase-linter/docs/rules/no-schema-name",version:"current",sidebar:"docs",previous:{title:"no-preconditions",permalink:"/liquibase-linter/docs/rules/no-preconditions"},next:{title:"no-raw-sql",permalink:"/liquibase-linter/docs/rules/no-raw-sql"}},s=[{value:"Why?",id:"why",children:[]},{value:"Options",id:"options",children:[]}],l={rightToc:s};function u(e){var n=e.components,t=Object(a.a)(e,["components"]);return Object(o.b)("wrapper",Object(r.a)({},l,t,{components:n,mdxType:"MDXLayout"}),Object(o.b)("h2",{id:"why"},"Why?"),Object(o.b)("p",null,"Adding a schema name can harm portability of scripts and databases if schema name is not used consistently across all instances. The schema name should not really be needed at the script level, as when running Liquibase the user should have the correct schema access and/or default schema anyway."),Object(o.b)("p",null,"The ",Object(o.b)("inlineCode",{parentName:"p"},"no-schema-name")," rule will fail for any changes that have a populated ",Object(o.b)("inlineCode",{parentName:"p"},"schemaName")," attribute."),Object(o.b)("h2",{id:"options"},"Options"),Object(o.b)("p",null,"No extra options."))}u.isMDXComponent=!0}}]);