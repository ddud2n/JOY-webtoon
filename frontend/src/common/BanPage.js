import React, { Component } from 'react';
import './NotFound.css';
import { Link } from 'react-router-dom';
import { Button } from 'antd';

class BanPage extends Component {
    render() {
        return (
            <div className="page-not-found">
                <h1 className="title11">
                    Ban Page
                </h1>
                <div className="desc">
                    The Page you're looking for was banned.
                </div>
                <Link to="/"><Button className="go-back-btn" type="primary" size="large">Go Back</Button></Link>
            </div>
        );
    }
}
export default BanPage;