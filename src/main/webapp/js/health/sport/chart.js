//////////////////////////////
/////     Sport Chart    /////
//////////////////////////////
//   @author Jonathan       //
//   @version 1.0           //
//   @since 28/12/2024      //
//////////////////////////////

// Paramétrage du chart
var ctx = document.getElementById("sportChart");
var myChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: data_date,
        datasets: [{
            label: "Sport",
			lineTension: 0.3,
            fill: false,
            backgroundColor: "rgba(39,106,162,1)",
            borderColor: "rgba(39,106,162,1)",
            pointRadius: 5,
            pointBackgroundColor: "rgba(39,106,162,1)",
            pointBorderColor: "rgba(255,255,255,0.8)",
            pointHoverRadius: 5,
            pointHoverBackgroundColor: "rgba(39,106,162,1)",
            pointHitRadius: 10,
            pointBorderWidth: 2,
            data: data_sport
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
                    maxTicksLimit: 24
                }
            },
            y: {
				beginAtZero: true,
				max: 1.25,
				min: -0.25,
                ticks: {
					color: '#888',
					stepSize: 1,
					callback: function(value, index, values) {
                    	if (value === 0) return 'Repos';
                        if (value === 1) return 'Activité';
                        return '';
                    }
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
	var url = '/health/sports?action=list&from=';
	url += dateFrom;
	url += '&to=';
	url += dateTo;
	document.location.href = url;
}
