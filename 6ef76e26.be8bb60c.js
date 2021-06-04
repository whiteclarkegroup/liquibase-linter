(window.webpackJsonp=window.webpackJsonp||[]).push([[24],{112:function(e,t,n){"use strict";n.d(t,"a",(function(){return p})),n.d(t,"b",(function(){return m}));var a=n(0),i=n.n(a);function r(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function o(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function l(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?o(Object(n),!0).forEach((function(t){r(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):o(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function c(e,t){if(null==e)return{};var n,a,i=function(e,t){if(null==e)return{};var n,a,i={},r=Object.keys(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||(i[n]=e[n]);return i}(e,t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(i[n]=e[n])}return i}var s=i.a.createContext({}),u=function(e){var t=i.a.useContext(s),n=t;return e&&(n="function"==typeof e?e(t):l(l({},t),e)),n},p=function(e){var t=u(e.components);return i.a.createElement(s.Provider,{value:t},e.children)},b={inlineCode:"code",wrapper:function(e){var t=e.children;return i.a.createElement(i.a.Fragment,{},t)}},h=i.a.forwardRef((function(e,t){var n=e.components,a=e.mdxType,r=e.originalType,o=e.parentName,s=c(e,["components","mdxType","originalType","parentName"]),p=u(n),h=a,m=p["".concat(o,".").concat(h)]||p[h]||b[h]||r;return n?i.a.createElement(m,l(l({ref:t},s),{},{components:n})):i.a.createElement(m,l({ref:t},s))}));function m(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var r=n.length,o=new Array(r);o[0]=h;var l={};for(var c in t)hasOwnProperty.call(t,c)&&(l[c]=t[c]);l.originalType=e,l.mdxType="string"==typeof e?e:a,o[1]=l;for(var s=2;s<r;s++)o[s]=n[s];return i.a.createElement.apply(null,o)}return i.a.createElement.apply(null,n)}h.displayName="MDXCreateElement"},82:function(e,t,n){"use strict";n.r(t),n.d(t,"frontMatter",(function(){return o})),n.d(t,"metadata",(function(){return l})),n.d(t,"rightToc",(function(){return c})),n.d(t,"default",(function(){return u}));var a=n(2),i=n(6),r=(n(0),n(112)),o={title:"Implementing a Custom Rule"},l={unversionedId:"custom-rules",id:"custom-rules",isDocsHomePage:!1,title:"Implementing a Custom Rule",description:"Whilst Liquibase Linter has lots of good core rules, you may have a use case that's particular to your project and wouldn't make sense as a core rule.",source:"@site/docs/custom-rules.md",slug:"/custom-rules",permalink:"/liquibase-linter/docs/custom-rules",version:"current",sidebar:"docs",previous:{title:"Using Rules",permalink:"/liquibase-linter/docs/rules/"},next:{title:"create-column-no-define-primary-key",permalink:"/liquibase-linter/docs/rules/create-column-no-define-primary-key"}},c=[{value:"Example use case",id:"example-use-case",children:[]},{value:"Writing the rule",id:"writing-the-rule",children:[]},{value:"Making the rule discoverable",id:"making-the-rule-discoverable",children:[]},{value:"Configuring the rule",id:"configuring-the-rule",children:[]}],s={rightToc:c};function u(e){var t=e.components,n=Object(i.a)(e,["components"]);return Object(r.b)("wrapper",Object(a.a)({},s,n,{components:t,mdxType:"MDXLayout"}),Object(r.b)("p",null,"Whilst Liquibase Linter has lots of good core rules, you may have a use case that's particular to your project and wouldn't make sense as a core rule."),Object(r.b)("p",null,"Fortunately it's trivial to implement custom rules and apply them in your own project; you just need to write a Java class implementing one of our rule interfaces, and do a little configuration. "),Object(r.b)("h2",{id:"example-use-case"},"Example use case"),Object(r.b)("p",null,"Let's say for argument's sake that we have a Liquibase project for an app. We ship customised implementations of this app to different clients, so we have a ",Object(r.b)("inlineCode",{parentName:"p"},"core")," context and then client-specific contexts like ",Object(r.b)("inlineCode",{parentName:"p"},"client_jane")," and ",Object(r.b)("inlineCode",{parentName:"p"},"client_john"),"."),Object(r.b)("p",null,"In the database for our app, we have a table called ",Object(r.b)("inlineCode",{parentName:"p"},"FORM_LAYOUT")," that holds configuration for how forms are laid out, which is always different for each client. One day, somebody makes a mistake and does a Liquibase change script to update ",Object(r.b)("inlineCode",{parentName:"p"},"FORM_LAYOUT")," for Jane, but accidentally uses the ",Object(r.b)("inlineCode",{parentName:"p"},"core")," context. It slips through code review and torpedoes all other client's form layout configs."),Object(r.b)("p",null,"We want to stop this happening again at the source, so let's see if we can do it with a lint rule."),Object(r.b)("h2",{id:"writing-the-rule"},"Writing the rule"),Object(r.b)("p",null,"There are three interfaces you could implement when writing a custom rule in Java; which you use depends on what level you want to work at."),Object(r.b)("ul",null,Object(r.b)("li",{parentName:"ul"},Object(r.b)("a",Object(a.a)({parentName:"li"},{href:"https://github.com/whiteclarkegroup/liquibase-linter/blob/master/src/main/java/com/whiteclarkegroup/liquibaselinter/config/rules/ChangeRule.java"}),"ChangeRule")," for linting each individual change, useful when you want to prevent issues with the content of the change itself"),Object(r.b)("li",{parentName:"ul"},Object(r.b)("a",Object(a.a)({parentName:"li"},{href:"https://github.com/whiteclarkegroup/liquibase-linter/blob/master/src/main/java/com/whiteclarkegroup/liquibaselinter/config/rules/ChangeSetRule.java"}),"ChangeSetRule")," for linting each changeSet, useful when you want to check things like comments and contexts, or the overall content of a changeSet e.g. when you want ensure certain changes happen together, or in isolation"),Object(r.b)("li",{parentName:"ul"},Object(r.b)("a",Object(a.a)({parentName:"li"},{href:"https://github.com/whiteclarkegroup/liquibase-linter/blob/master/src/main/java/com/whiteclarkegroup/liquibaselinter/config/rules/ChangeLogRule.java"}),"ChangeLogRule")," for linting each changeLog file, useful when you want to check the overall content at changeLog level - rarely used in practise")),Object(r.b)("p",null,"The ",Object(r.b)("inlineCode",{parentName:"p"},"ChangeRule")," interface uses generics, so you can target a specific change type e.g. ",Object(r.b)("inlineCode",{parentName:"p"},"implements ChangeRule<InsertDataChange>")," for inserts, or just do ",Object(r.b)("inlineCode",{parentName:"p"},"implements ChangeRule<Change>")," to catch all changes. You can also use this to lint ",Object(r.b)("a",Object(a.a)({parentName:"p"},{href:"http://www.liquibase.org/documentation/changes/custom_change.html"}),"custom changes"),", if you have any of those in your project."),Object(r.b)("p",null,"It's worth noting that the three levels are not isolated from one another - a ",Object(r.b)("inlineCode",{parentName:"p"},"Change")," has access to the ",Object(r.b)("inlineCode",{parentName:"p"},"ChangeSet")," it belongs to, which in turn can access the ",Object(r.b)("inlineCode",{parentName:"p"},"ChangeLog")," it belongs to, and vice versa, so you can freely traverse to get the information you need to decide if your rule is failed."),Object(r.b)("p",null,"For our ",Object(r.b)("inlineCode",{parentName:"p"},"FORM_LAYOUT")," use case, it makes the most sense to lint at changeSet level. Here's the code to implement it:"),Object(r.b)("pre",null,Object(r.b)("code",Object(a.a)({parentName:"pre"},{className:"language-java"}),'package com.fake.fancyapp.liquibase;\n\nimport com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;\nimport com.whiteclarkegroup.liquibaselinter.config.rules.ChangeSetRule;\nimport liquibase.change.core.UpdateDataChange;\nimport liquibase.changelog.ChangeSet;\n\npublic class FormLayoutContextRuleImpl extends AbstractLintRule implements ChangeSetRule {\n    private static final String NAME = "form-layout-context";\n    private static final String MESSAGE = "FORM_LAYOUT should only ever be updated in a client-specific context!";\n\n    public FormLayoutContextRuleImpl() {\n        super(NAME, MESSAGE);\n    }\n\n    @Override\n    public boolean invalid(ChangeSet changeSet) {\n        return isForFormLayout(changeSet) && isCoreContext(changeSet);\n    }\n\n    private boolean isForFormLayout(ChangeSet changeSet) {\n        return changeSet.getChanges().stream()\n            .anyMatch(change -> change instanceof UpdateDataChange && "FORM_LAYOUT".equals(((UpdateDataChange) change).getTableName()));\n    }\n\n    private boolean isCoreContext(ChangeSet changeSet) {\n        return changeSet.getContexts().getContexts().stream().anyMatch("core"::equals);\n    }\n}\n')),Object(r.b)("p",null,"Some notes about how we've done this:"),Object(r.b)("ul",null,Object(r.b)("li",{parentName:"ul"},"We've extended the ",Object(r.b)("inlineCode",{parentName:"li"},"AbstractLintRule")," class, which saves us fussing about a lot of boilerplate ourselves. We just need to pass our rule name (i.e. the key uses in the ",Object(r.b)("a",Object(a.a)({parentName:"li"},{href:"/liquibase-linter/docs/rules/"}),"rules config"),") and the failure message"),Object(r.b)("li",{parentName:"ul"},"The key method we need to implement ourselves is ",Object(r.b)("inlineCode",{parentName:"li"},"invalid")," - this accepts a ",Object(r.b)("a",Object(a.a)({parentName:"li"},{href:"https://github.com/liquibase/liquibase/blob/master/liquibase-core/src/main/java/liquibase/changelog/ChangeSet.java"}),"Liquibase ",Object(r.b)("inlineCode",{parentName:"a"},"ChangeSet"))," and should return true if the rule has ",Object(r.b)("em",{parentName:"li"},"failed")," - in this case it will do so if any of the changes are an update on the ",Object(r.b)("inlineCode",{parentName:"li"},"FORM_LAYOUT")," table ",Object(r.b)("em",{parentName:"li"},"and")," the ",Object(r.b)("inlineCode",{parentName:"li"},"core")," context is used")),Object(r.b)("p",null,"All the core rules are implemented in this way as well, so if you're not sure how best to hook something up you might try looking in the source at ",Object(r.b)("a",Object(a.a)({parentName:"p"},{href:"https://github.com/whiteclarkegroup/liquibase-linter/tree/master/src/main/java/com/whiteclarkegroup/liquibaselinter/config/rules/core"}),"some existing core rules")," that do something similar"),Object(r.b)("h2",{id:"making-the-rule-discoverable"},"Making the rule discoverable"),Object(r.b)("p",null,"Now, our class above should go into a new Maven project that depends on both ",Object(r.b)("inlineCode",{parentName:"p"},"liquibase-linter")," and ",Object(r.b)("inlineCode",{parentName:"p"},"liquibase"),"."),Object(r.b)("p",null,"The class existing isn't quite enough on its own; we need to tell Liquibase Linter that it's there. For this we are using the ",Object(r.b)("a",Object(a.a)({parentName:"p"},{href:"https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html"}),"Service Provider Interface")," pattern - this is natively supported in Java and for use cases like this is preferable to powerful-but-heavy classpath scanning approaches like the one used by Spring."),Object(r.b)("p",null,"In our newly-created project, we'll create a new file at:"),Object(r.b)("p",null,Object(r.b)("inlineCode",{parentName:"p"},"src/main/resources/META-INF/services/com.whiteclarkegroup.liquibaselinter.config.rules.ChangeSetRule")),Object(r.b)("p",null,"And in the file, we'll write:"),Object(r.b)("p",null,Object(r.b)("inlineCode",{parentName:"p"},"com.fake.fancyapp.liquibase.FormLayoutContextRuleImpl")),Object(r.b)("h2",{id:"configuring-the-rule"},"Configuring the rule"),Object(r.b)("p",null,"In the project where our scripts live, we'll add a dependency on our rules project to ",Object(r.b)("inlineCode",{parentName:"p"},"liquibase-maven-plugin"),", in much the same way that we ",Object(r.b)("a",Object(a.a)({parentName:"p"},{href:"/liquibase-linter/docs/configure"}),"added a dependency for ",Object(r.b)("inlineCode",{parentName:"a"},"liquibase-linter")," originally"),":\nSo for our example custom rules project ",Object(r.b)("inlineCode",{parentName:"p"},"wcg-liquibase-linter")," we would have the following dependency."),Object(r.b)("pre",null,Object(r.b)("code",Object(a.a)({parentName:"pre"},{className:"language-xml"}),"<plugin>\n    <groupId>org.liquibase</groupId>\n    <artifactId>liquibase-maven-plugin</artifactId>\n    <configuration>\n        ...\n    </configuration>\n    <dependencies>\n        <dependency>\n            <groupId>com.whiteclarkegroup</groupId>\n            <artifactId>liquibase-linter</artifactId>\n        </dependency>\n        <dependency>\n            <groupId>com.fake.fancyapp</groupId>\n            <artifactId>liquibase-linter-rules</artifactId>\n            <version>0.1.0</version>\n        </dependency>\n    </dependencies>\n    <executions>\n        ...\n    </executions>\n</plugin>\n")),Object(r.b)("p",null,"Then all we need is to ",Object(r.b)("a",Object(a.a)({parentName:"p"},{href:"/liquibase-linter/docs/rules/"}),"configure the rule as normal")," in ",Object(r.b)("inlineCode",{parentName:"p"},"lqlint.json"),"."))}u.isMDXComponent=!0}}]);