import React, { Component } from 'react';
import { callAPI } from "../../Tools";

import './club.css';

class ClubNew extends Component {
    constructor(props) {
        super(props);
        this.state = {
            clubName:"",
            members: [""]
        };

        this.changeClubName = this.changeClubName.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    createAbbr(name){
        name = name.trim();
        var words = name.split(" ");
        var abbr = "";
        if (words.length === 1){
            abbr = name.substring(0,3);
        } else if (words.length === 2) {
            abbr = words[0].substring(0,2)+words[1].substring(0,2);
        } else {    
            words.forEach(element => {
                abbr = abbr + element.substring(0,1)
            });
        }

        return abbr.toUpperCase();
    }

    changeClubName(event) {
        this.setState({clubName: event.target.value});
    }

    changeMemberName(event) {
        var memberArray = this.state.members;
        var i = event.target.getAttribute("index")
        memberArray[i] = event.target.value;
        this.setState({
            members: memberArray
        });
    }

    addMember() {
        var newMember = "";
        this.setState(prevState => ({ members: prevState.members.concat([newMember]) }));
    }

    removeMember(index) {
        var memberArray = this.state.members;
        memberArray[index] = null;
        this.setState({
            members: memberArray
        });
    }

    handleSubmit(event) {
        event.preventDefault();

        // club name cannot be null
        if (this.state.clubName === null || this.state.clubName.trim() === "") {
            alert("club name cannot be null");
            return;
        }

        // get abbr of this club name
        var abbr = this.createAbbr(this.state.clubName)

        // post new club
        var memberNames = []
        this.state.members.reduce(function(prev, value) {
            if (value !== null && value.trim() !== "") {
                memberNames.push({"name":value});
            }
            return prev;
        },[])
        var postData = {
            "club":{
                "name":this.state.clubName,
                "abbr":abbr
            },
            "members":memberNames
        }
        callAPI("/api/club", "POST", postData).then(data => {
            console.log(data)
            alert("submitted!")
        })
    }

    render() {
        return (
            <div>
                <form onSubmit={this.handleSubmit}>
                    <div className="sub-title">New Club</div>
                    <label>
                        Club Name <input type="text" name="name" value={this.state.clubName} onChange={this.changeClubName}/>
                    </label>
                    <div className="sub-title">New Members</div>
                    { this.state.members.map((value,index) => {
                        if (value != null) {
                            return <div key={index} className="inline">
                                <label>
                                    Member Name 
                                    <input index={index} type="text" name="member" value={value} onChange={event => this.changeMemberName(event)}/>
                                </label>
                                <button type="button" name="remove" onClick={() => this.removeMember(index)}>remove</button>
                            </div>;
                        } else{
                            return null;
                        }
                    })}
                    <div className="inline">
                        <button type="button" onClick={() => this.addMember()}>add a member</button>    
                        <input type="submit" value="Submit" />
                    </div>
                </form>
            
            </div>
        );
    }
}

export default ClubNew;
