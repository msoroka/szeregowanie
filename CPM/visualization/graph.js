window.onload = function () {
    fetch("../graph.json")
        .then(response => response.json())
        .then(json => jsonIteration(json));

    let jsonIteration = (json) => {
        for (let key in json) {
            if (json.hasOwnProperty(key)) {
                console.log(key, json[key]);
            }
        }
    };
}