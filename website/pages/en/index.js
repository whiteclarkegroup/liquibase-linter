/**
 * Copyright (c) 2017-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

const React = require('react');

const CompLibrary = require('../../core/CompLibrary.js');

const MarkdownBlock = CompLibrary.MarkdownBlock;
/* Used to read markdown */
const Container = CompLibrary.Container;
const GridBlock = CompLibrary.GridBlock;

const siteConfig = require(`${process.cwd()}/siteConfig.js`);

function imgUrl(img) {
    return `${siteConfig.baseUrl}img/${img}`;
}

function docUrl(doc, language) {
    return `${siteConfig.baseUrl}docs/${language ? `${language}/` : ''}${doc}`;
}

function pageUrl(page, language) {
    return siteConfig.baseUrl + (language ? `${language}/` : '') + page;
}

class Button extends React.Component {
    render() {
        return (
            <div className="pluginWrapper buttonWrapper">
                <a className="button" href={this.props.href} target={this.props.target}>
                    {this.props.children}
                </a>
            </div>
        );
    }
}

Button.defaultProps = {
    target: '_self',
};

const SplashContainer = props => (
    <div className="homeContainer">
        <div className="homeSplashFade">
            <div className="wrapper homeWrapper">{props.children}</div>
        </div>
    </div>
);

const Logo = props => (
    <div className="projectLogo">
        <img src={props.img_src} alt="Project Logo"/>
    </div>
);

const ProjectTitle = () => (
    <h2 className="projectTitle">
        {siteConfig.title}
        <small>{siteConfig.tagline}</small>
    </h2>
);

const PromoSection = props => (
    <div className="section promoSection">
        <div className="promoRow">
            <div className="pluginRowBlock">{props.children}</div>
        </div>
    </div>
);

class HomeSplash extends React.Component {
    render() {
        return (
            <SplashContainer>
                <div className="inner">
                    <ProjectTitle/>
                    <PromoSection>
                        <Button href="#start">Get Started</Button>
                    </PromoSection>
                </div>
            </SplashContainer>
        );
    }
}

const Block = props => (
    <Container
        padding={['bottom', 'top']}
        id={props.id}
        background={props.background}>
        <GridBlock align="center" contents={props.children} layout={props.layout}/>
    </Container>
);

const Features = () => (
    <Block layout="threeColumn" background="light">
        {[
            {
                content: 'Prevent changes that will cause issues at the source, before they make it into any of your databases',
                image: imgUrl('prevent-icon.svg'),
                imageAlign: 'top',
                title: 'Prevent Issues',
            },
            {
                content: 'Get more consistency and higher quality in your scripts, while educating your developers about best practises',
                image: imgUrl('improve-icon.svg'),
                imageAlign: 'top',
                title: 'Improve Quality',
            },
            {
                content: 'Choose which rules you enable and how they are applied, so your checks are aligned with your project\'s needs',
                image: imgUrl('configure-icon.svg'),
                imageAlign: 'top',
                title: 'Configure & Customise',
            }
        ]}
    </Block>
);

const Start = () => (
    <Container
        id="start"
        padding={['bottom', 'top']}>
        <article>
            <h2>Get Started</h2>
<MarkdownBlock>
**1** Add `liquibase-linter` to your pom as a dependency of `liquibase-maven-plugin`:
</MarkdownBlock>
<MarkdownBlock>{`\`\`\`xml
<plugin>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-maven-plugin</artifactId>
    <configuration>
        ...
    </configuration>
    <dependencies>
        <dependency>
            <groupId>com.whiteclarkegroup</groupId>
            <artifactId>liquibase-linter</artifactId>
            <version>0.2.1</version>
        </dependency>
    </dependencies>
    <executions>
        ...
    </executions>
</plugin>
\`\`\``}</MarkdownBlock>
<MarkdownBlock>
**2** Add the [config file](examples/lqlint.json) to your project root, and start [turning on rules](docs/rules).
</MarkdownBlock>
        </article>
    </Container>
);

class Index extends React.Component {
    render() {
        return (
            <div>
                <HomeSplash/>
                <div className="mainContainer">
                    <Features/>
                    <Start/>
                </div>
            </div>
        );
    }
}

module.exports = Index;
