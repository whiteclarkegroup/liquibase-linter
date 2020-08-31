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
            items: [
                {to: 'install', label: 'Setup'},
                {to: 'rules/index', label: 'Rules'},
                {to: 'blog', label: 'Blog'},
                {href: 'https://github.com/whiteclarkegroup/liquibase-linter', label: 'GitHub'}
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
