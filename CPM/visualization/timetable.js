window.onload = function () {
    fetch("../timetable.json")
        .then(response => response.json())
        .then(json => jsonIteration(json));

    let jsonIteration = (json) => {
        let data = [];
        for (let key in json) {
            if (json.hasOwnProperty(key)) {
                data.push({
                    x: json[key].machine,
                    y: [json[key].values.earliestStarts, json[key].values.earliestFinishes],
                    label: key,
                })
            }
        }

        console.log(data);

        let chart = new CanvasJS.Chart("timetableChart", {
            animationEnabled: false,
            exportEnabled: false,
            indexLabelPlacement: "inside",
            title: {
                text: "Harmonogram"
            },
            axisX: {
                title: "Maszyny",
                labelFormatter: function(e){
                    return  "M: " + e.value;
                }
            },
            axisY: {
                includeZero: false,
                title: "Czas trwania",
                interval: 1
            },
            data: [{
                type: "rangeBar",
                dataPoints: data,
            }]
        });
        chart.render();
    };
}