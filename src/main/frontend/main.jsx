import React from 'react'
import ReactDOM from 'react-dom'
import ReactDOMServer from 'react-dom/server'
import {StaticRouter} from 'react-router';
import {BrowserRouter} from 'react-router-dom';
import createBrowserHistory from 'history/createBrowserHistory';
import createMemoryHistory from 'history/createMemoryHistory';
import Body from './js/component/layout/Body.jsx'

class Frontend {
    renderServer = (location, jsonModel) => {
        let model = JSON.parse(jsonModel);
        return ReactDOMServer.renderToString(
            <StaticRouter location={location} context={model}>
                <Body model={model} />
            </StaticRouter>
        );
    }

    renderClient = (model) => {
        let history = createBrowserHistory();
        return ReactDOM.render(
            <BrowserRouter>
                <Body history={history} model={model} />
            </BrowserRouter>,
            document.getElementById('body')
        );
    }
}

exports.Frontend = Frontend;