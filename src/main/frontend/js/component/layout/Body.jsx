import React from 'react';
import Header from './Header.jsx'
import Content from './Content.jsx'

class Body extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            model: this.props.model
        }
    }

    render() {
        return (
            <div>
                <Header />
                <Content content={this.state.model.content} />
            </div>
        )
    }
}

module.exports = Body;