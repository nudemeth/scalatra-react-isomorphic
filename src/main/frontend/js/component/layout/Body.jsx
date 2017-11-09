import React from 'react';
import Header from './Header.jsx'
import Content from './Content.jsx'

class Body extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <Header />
                <Content {...this.props} />
            </div>
        )
    }
}

module.exports = Body;