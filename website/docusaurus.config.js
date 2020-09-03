module.exports = {
    title: 'Liquibase Linter',
    tagline: 'The configurable, extensible linter for Liquibase',
    url: 'https://liquibase-linter.dev',
    baseUrl: '/',
    projectName: 'liquibase-linter',
    organizationName: 'whiteclarkegroup',
    favicon: 'img/favicon.ico',
    scripts: ['//buttons.github.io/buttons.js'],
    stylesheets: ['//fonts.googleapis.com/css2?family=Cabin:ital,wght@0,400;0,700;1,400;1,700&family=Inconsolata&display=swap'],
    presets: [
        [
            '@docusaurus/preset-classic',
            {
                theme: {
                    customCss: require.resolve('./src/css/custom.css'),
                },
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
        colorMode: {
            defaultMode: 'light',
            disableSwitch: true
        },
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
                alt: 'White Clarke Group Logo',
                src: '/img/wcg-logo.svg',
                href: 'https://whiteclarkegroup.com/',
            },
            copyright: `Copyright © ${new Date().getFullYear()} White Clarke Group`,
        },
        image: 'img/docusaurus.png',
        sidebarCollapsible: false,
    },
};
