import React, { Component } from 'react';
import { callAPI } from "../../Tools";

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
            <div>
                <div>Clubs</div>
                <div>
                    {this.state.allData.map(function(data,i){
                        return <div key={i}>
                            <ul>
                                <li>club id: {data[0].id}</li>
                                <li>club name: {data[0].name}</li>
                                <li>club abbr.: {data[0].abbr}</li>
                            </ul>
                            {data[1].map(function(member,i){
                                if ( member != null) {
                                    return <ul key={i}>
                                        <li>member id:{member.id}</li>
                                        <li>member name:{member.name}</li>
                                    </ul>;
                                } else {
                                    return null
                                }
                            })}
                        </div>;
                        
                    })}
                </div>
            </div>
        );
    }
}
export default ClubList;
