import React from 'react';
import {Route, Link} from 'react-router-dom'
import Routes from '../route/Routes.jsx';

class Content extends React.Component {
    constructor(props) {
        super(props);
    }

    renderComponent = (component) => {
        return (
            <div>
                {component(this.props.model)}
            </div>
        );
    }

    renderRoutes = () => {
        return Routes.map((route, index) => (
            <Route key={index} path={route.path} exact={route.exact} render={() => this.renderComponent(route.component)} />
        ));
    }

    render() {
        return (
            <main role="main">
                {this.renderRoutes()}
            </main>
        );
    }
}

module.exports = Content;