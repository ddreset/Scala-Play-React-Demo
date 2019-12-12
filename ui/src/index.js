import React from 'react';
import ReactDOM from 'react-dom';
import {
    BrowserRouter as Router,
    Route,
    Link,
    Switch
} from 'react-router-dom';
import './index.css';
import App from './App';
import ClubNew from './components/Club/New';
import ClubList from './components/Club/List';
import Notfound from './notfound'
import * as serviceWorker from './serviceWorker';

const routing = (
    <Router>
        <div>
            <ul>
                <li>
                    <Link to="/">List Clubs</Link>
                </li>
                <li>
                    <Link to="/newClub">Create New Club</Link>
                </li>
                <li>
                    <Link to="/app">scala-play-react-seed</Link>
                </li>
            </ul>
            <Switch>
                <Route exact path="/" component={ClubList} />
                <Route path="/newClub" component={ClubNew} />
                <Route path="/app" component={App} />
                <Route component={Notfound} />
            </Switch>
        </div>
    </Router>
)

ReactDOM.render(routing, document.getElementById('root'))

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
