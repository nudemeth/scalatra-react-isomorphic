import React from 'react';
import Home from './../view/Home.jsx'
import About from './../view/About.jsx'

const routes = [
    { path: '/', exact: true, component: (model) => <Home model={model} /> },
    { path: '/about', component: (model) => <About model={model} /> }
];

module.exports = routes;