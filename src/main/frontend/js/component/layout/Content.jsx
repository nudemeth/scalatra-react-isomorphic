import React from 'react';

class Content extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <main role="main">
                {this.props.content}
            </main>
        );
    }
}

module.exports = Content;