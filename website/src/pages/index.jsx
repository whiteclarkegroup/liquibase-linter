import React from 'react';
import GitHubButton from 'react-github-btn'
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import useBaseUrl from '@docusaurus/useBaseUrl';
import Layout from '@theme/Layout';
import Feature from "../components/Feature";

const MyPage = () => {
    const {siteConfig} = useDocusaurusContext();
    return (
        <Layout
            permalink="/"
            title={siteConfig.title}
            description={siteConfig.tagline}>
            <div className="lqlint-hero">
                <div className="container padding-vert--xl">
                    <div className="lqlint-intro">
                        <h1 className="lqlint-tagline">{siteConfig.tagline}</h1>
                        <div className="lqlint-actions">
                            <Link
                                to={useBaseUrl('/docs/install')}
                                className="lqlint-cta button button--lg button--primary">
                                Get started
                            </Link>
                            <GitHubButton href="https://github.com/whiteclarkegroup/liquibase-linter" data-size="large"
                                          aria-label="Star liquibase-linter on GitHub">Star</GitHubButton>
                        </div>
                    </div>
                </div>
            </div>
            <main>
                <div className="container text--center margin-vert--xl">
                    <div className="row">
                        <div className="col">
                            <Feature illustration="tools"
                                     title="Works with your tools">
                                Slots right into your Maven or Gradle project with a small dependency footprint.
                            </Feature>
                        </div>
                        <div className="col">
                            <Feature illustration="setup"
                                     title="Painless setup">
                                Easily retrofit to an existing project without rewriting anything.
                            </Feature>
                        </div>
                        <div className="col">
                            <Feature illustration="rules"
                                     title="Custom rules">
                                Write custom rules to fit your needs, and share them across your projects.
                            </Feature>
                        </div>
                    </div>
                </div>
            </main>
        </Layout>
    );
};

export default MyPage;
