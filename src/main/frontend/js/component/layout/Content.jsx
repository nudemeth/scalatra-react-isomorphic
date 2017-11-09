import React from 'react';
import {Route, Link} from 'react-router-dom'
import Routes from '../navigation/Routes.jsx';

class Content extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <main role="main">
                {Routes.map((route, index) => (
                    <Route key={index} path={route.path} exact={route.exact} render={() => (<div>{route.component(this.props.model)}</div>)} />
                ))}
            </main>
        );
    }
}

module.exports = Content;