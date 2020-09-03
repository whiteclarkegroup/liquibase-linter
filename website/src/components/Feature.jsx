import React from "react";
import useBaseUrl from '@docusaurus/useBaseUrl';

export default function Feature(props) {
    return <>
        <img className="lqlint-feature-illustration" alt="" src={useBaseUrl(`img/${props.illustration}.svg`)}/>
        <h2 className="lqlint-feature-title">{props.title}</h2>
        <p className="padding-horiz--md">{props.children}</p>
    </>
};
