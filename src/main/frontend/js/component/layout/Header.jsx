import React from 'react';

class Header extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <header>
                <h1>This is header</h1>
                <ul>
                    <li><a href="#">Link 1</a></li>
                    <li><a href="#">Link 2</a></li>
                </ul>
            </header>
        );
    }
}

module.exports = Header;