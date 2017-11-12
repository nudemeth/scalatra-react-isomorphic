import React from 'react';

class About extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            model: this.props.model || {text: ''}
        }
    }

    loadModelFromServer = () => {
        let url = '/data/about';
        $.ajax({
            url: url,
            contentType: 'application/json',
            dataType: 'json',
            type: 'POST',
            success: (result) => {
                this.setState({model: result});
            },
            failure: (xhr, status, err) => {
                console.error(url, status, err.toString());
            }
        });
    }

    componentDidMount() {
        this.loadModelFromServer();
    }

    render() {
        return (
            <h2>{this.state.model.text}</h2>
        );
    }
}

module.exports = About;