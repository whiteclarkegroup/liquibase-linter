import React from 'react';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import useBaseUrl from '@docusaurus/useBaseUrl';
import Layout from '@theme/Layout';

const MyPage = () => {
    const {siteConfig} = useDocusaurusContext();
    return (
        <Layout
            permalink="/"
            title={siteConfig.title}
            description={siteConfig.tagline}>
            <div className="lqlint-hero">
                <div className="lqlint-intro">
                    <h1 className="lqlint-title">{siteConfig.title}</h1>
                    <h2 className="lqlint-tagline">{siteConfig.tagline}</h2>
                    <Link
                        to={useBaseUrl('/docs/install')}
                        className="button button--lg button--primary">
                        Get started
                    </Link>
                </div>
                <div className="lqlint-pitch">
                    <p className="lqlint-benefit">
                        <strong>Prevent</strong> changes that will cause issues at the source, before they make it into
                        any of your databases.
                    </p>
                    <p className="lqlint-benefit">
                        <strong>Configure</strong> your setup and rules to suit your project's needs &mdash; and easily
                        retrofit without rewriting existing code.
                    </p>
                    <p className="lqlint-benefit">
                        <strong>Educate</strong> developers about best practices and improve consistency in your
                        codebase.
                    </p>
                </div>
            </div>
        </Layout>
    );
};

export default MyPage;
