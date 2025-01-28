///////////////////////////////
/////     Weight Chart    /////
///////////////////////////////
//   @author Jonathan        //
//   @version 1.1            //
//   @since 28/12/2024       //
///////////////////////////////

// Paramétrage du chart
var ctx = document.getElementById("weightChart");
var myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: data_date,
        datasets: [{
            label: "Poids",
            lineTension: 0.3,
            fill: false,
            backgroundColor: "rgba(39,106,162,1)",
            borderColor: "rgba(39,106,162,1)",
            pointRadius: 3,
            pointBackgroundColor: "rgba(39,106,162,1)",
            pointBorderColor: "rgba(255,255,255,0.8)",
            pointHoverRadius: 6,
            pointHoverBackgroundColor: "rgba(39,106,162,1)",
            pointHitRadius: 10,
            pointBorderWidth: 1,
            data: data_weight
        }],
    },
    options: {
        scales: {
            x: {
                type: 'time',
                time: {
                    unit: 'month',
                    displayFormats: {
                        day: 'MM/YY'
                    }
                },
                grid: {
                    display: false
                },
                ticks: {
					color: '#888',
                    maxTicksLimit: 25
                }
            },
            y: {
				suggestedMin: 65,
				suggestedMax: 90,
                ticks: {
					color: '#888',
					stepSize: 5,
                    maxTicksLimit: 30
                },
                grid: {
                    color: "rgba(0, 0, 0, 1)",
                }
            }
        },
        plugins: {
            legend: {
                display: false
            }
        },
        locale: 'fr-FR'
    }
});

/* Period Filter */
function updatePeriod() {
	
	// Get variables
	var selectFromDateTime = $('#selectFromDateTime');
	var selectToDateTime = $('#selectToDateTime');

	// Parse dates
	var dateFromValue = selectFromDateTime.val();
	var dateToValue = selectToDateTime.val();
	var dateFrom;
	var dateTo;
	var now = new Date();
	if (dateFromValue != null && dateFromValue.length == 16) {
		dateFrom = Date.parse(dateFromValue);
	} else {
		dateFrom = Date.parse(now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + now.getDate() + 'T' + now.getHours() + ':' + now.getMinutes());
	}
	if (dateToValue != null && dateToValue.length == 16) {
		dateTo = Date.parse(dateToValue);
	} else {
		dateTo = Date.parse(now.getFullYear() + '-' + (now.getMonth() + 1)  + '-' + now.getDate() + 'T' + now.getHours() + ':' + now.getMinutes());
	}
	
	// Redirect
	var url = path + '/health/weights?action=list&from=';
	url += dateFrom;
	url += '&to=';
	url += dateTo;
	document.location.href = url;
}
