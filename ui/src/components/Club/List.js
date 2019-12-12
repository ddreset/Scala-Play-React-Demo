import React, { Component } from 'react';
import { callAPI } from "../../Tools";

import './club.css';

class ClubList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            allData: []
        };
    }

    async componentDidMount() {
        callAPI("/api/club").then(data => {
            console.log(data)
            this.setState({
                allData: data
            });
        })
    }

    render() {
        return (
            <div className="content">
                <div className="title">Clubs</div>
                <div className="column">
                    {this.state.allData.map(function(data,i){
                        return <div key={i} className="column">
                            <div>CLUB {data[0].id}: {data[0].name} ({data[0].abbr})</div>
                            <div className="inline">
                                {data[1].map(function(member,i){
                                    if ( member != null) {
                                        return <ul key={i}>
                                            <li>member id: {member.id}</li>
                                            <li>member name: {member.name}</li>
                                        </ul>;
                                    } else {
                                        return null
                                    }
                                })}
                            </div>
                            
                        </div>;
                        
                    })}
                </div>
            </div>
        );
    }
}
export default ClubList;
