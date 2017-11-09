import React from 'react';

class Home extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <h2>{this.props.model.greeting}</h2>
        );
    }
}

module.exports = Home;