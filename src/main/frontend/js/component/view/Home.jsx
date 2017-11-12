import React from 'react';

class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            model: this.props.model || {greeting: ''}
        }
    }

    loadModelFromServer = () => {
        let url = '/data/home';
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

    componentDidMount = () => {
        this.loadModelFromServer();
    }

    render() {
        return (
            <h2>{this.state.model.greeting}</h2>
        );
    }
}

module.exports = Home;