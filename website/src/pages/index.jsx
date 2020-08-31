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
            <div className="hero text--center">
                <div className="container ">
                    <div className="padding-vert--md">
                        <h1 className="hero__title">{siteConfig.title}</h1>
                        <p className="hero__subtitle">{siteConfig.tagline}</p>
                    </div>
                    <div>
                        <Link
                            to={useBaseUrl('/docs/install')}
                            className="button button--lg button--outline button--primary">
                            Get started
                        </Link>
                    </div>
                </div>
            </div>
        </Layout>
    );
};

export default MyPage;
