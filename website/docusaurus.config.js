module.exports = {
    title: 'Liquibase Linter',
    tagline: 'Quality control for your Liquibase scripts',
    url: 'https://liquibase-linter.dev',
    baseUrl: '/',
    projectName: 'liquibase-linter',
    organizationName: 'whiteclarkegroup',
    favicon: 'img/favicon.ico',
    scripts: ['//buttons.github.io/buttons.js'],
    stylesheets: ['//fonts.googleapis.com/css?family=Lato'],
    presets: [
        [
            '@docusaurus/preset-classic',
            {
                docs: {
                    // Docs folder path relative to website dir.
                    path: './docs',
                    // Sidebars file relative to website dir.
                    sidebarPath: require.resolve('./sidebars.json'),
                },
            },
        ],
    ],
    themeConfig: {
        navbar: {
            title: 'Liquibase Linter',
            items: [
                {to: 'docs/install', label: 'Setup', position: 'right'},
                {to: 'docs/rules/index', label: 'Rules', position: 'right'},
                {to: 'blog', label: 'Blog', position: 'right'},
                {href: 'https://github.com/whiteclarkegroup/liquibase-linter', label: 'GitHub', position: 'right'}
            ],
        },
        footer: {
            logo: {
                alt: 'Facebook Open Source Logo',
                src: 'https://docusaurus.io/img/oss_logo.png',
                href: 'https://opensource.facebook.com/',
            },
            copyright: `Copyright © ${new Date().getFullYear()} White Clarke Group`,
        },
        image: 'img/docusaurus.png',
        sidebarCollapsible: false,
    },
};
