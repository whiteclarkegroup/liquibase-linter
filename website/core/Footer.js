/**
 * Copyright (c) 2017-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

const React = require('react');

class Footer extends React.Component {
  docUrl(doc, language) {
    const baseUrl = this.props.config.baseUrl;
    return `${baseUrl}docs/${language ? `${language}/` : ''}${doc}`;
  }

  pageUrl(doc, language) {
    const baseUrl = this.props.config.baseUrl;
    return baseUrl + (language ? `${language}/` : '') + doc;
  }

  render() {
    return (
      <footer className="nav-footer" id="footer">
        <section className="sitemap">
          <div>
            <h5>Docs</h5>
            <a href={this.docUrl('install', this.props.language)}>
              Setup
            </a>
            <a href={this.docUrl('rules/index', this.props.language)}>
              Rules
            </a>
          </div>
          <div>
            <h5>Code</h5>
              <a href={this.props.config.repoUrl}>GitHub</a>
              <a href="">
                  <img alt="Travis Build" src="//travis-ci.org/whiteclarkegroup/liquibase-linter.svg?branch=master" />
              </a>
              <a href="">
                  <img alt="Codacy Quality" src="//api.codacy.com/project/badge/Grade/320a8a4be4fd44feb9d6102ccdc7e240" />
              </a>
          </div>
          <div>
            <h5>More</h5>
            <a href={`${this.props.config.baseUrl}blog`}>Blog</a>
          </div>
        </section>

        <a
          href="https://www.whiteclarkegroup.com/"
          target="_blank"
          rel="noopener"
          className="fbOpenSource">
          <img
            src={`${this.props.config.baseUrl}img/wcg-logo.svg`}
            alt="White Clarke Group"/>
        </a>
        <section className="copyright">{this.props.config.copyright}</section>
      </footer>
    );
  }
}

module.exports = Footer;
