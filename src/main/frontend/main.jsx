import React from 'react'
import ReactDOM from 'react-dom'
import ReactDOMServer from 'react-dom/server'
import Body from './js/component/layout/Body.jsx'

class Frontend {
    renderServer = (jsonModel) => {
        let model = JSON.parse(jsonModel);
        return ReactDOMServer.renderToString(
            <Body model={model} />
        );
    }

    renderClient = (model) => {
        return ReactDOM.render(
            <Body model={model} />,
            document.getElementById('body')
        );
    }
}

exports.Frontend = Frontend;