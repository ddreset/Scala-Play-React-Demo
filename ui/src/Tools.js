function checkStatus(response) {
    if (response.status >= 200 && response.status < 300) {
        return response;
    }
    const error = new Error(`HTTP Error ${response.statusText}`);
    error.status = response.statusText;
    error.response = response;
    console.log(error); // eslint-disable-line no-console
    throw error;
}

function parseJSON(response) {
    console.log(response)
    return response.json();
}

export function callAPI(url, method = "GET", data = null) {
    const fetchParams = { method: method, 
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json'
        }}
    if (data != null) {
        fetchParams["body"] = JSON.stringify(data)
    };
    const fetchresponse = fetch(url, fetchParams).then(checkStatus).then(parseJSON)
    return fetchresponse
    
}