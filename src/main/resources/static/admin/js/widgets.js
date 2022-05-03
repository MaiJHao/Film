$(function () {
	"use strict";
	// chart 1
	var options = {
		series: [{
			name: '访问数',
			data: [10, 9, 24, 19, 22, 9, 12, 7]
		}],
		chart: {
			type: 'bar',
			width: 130,
			height: 65,
			sparkline: {
				enabled: true
			},
			stacked: true,
			toolbar: {
				show: false
			},
		},
		plotOptions: {
			bar: {
				horizontal: false,
				columnWidth: '25%',
				endingShape: 'rounded'
			},
		},
		legend: {
			position: 'top',
			horizontalAlign: 'left',
			offsetX: 0
		},
		dataLabels: {
			enabled: false
		},
		stroke: {
			show: true,
			width: 0,
			colors: ['transparent']
		},
		colors: ["#337ab7"],
		xaxis: {
			categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月'],
		},
		fill: {
			opacity: 1
		}
	};
	var chart = new ApexCharts(document.querySelector("#chart1"), options);
	chart.render();
	// chart 2
	var options = {
		series: [{
			name: '客户数',
			data: [10, 9, 24, 19, 22, 9, 12, 7]
		}],
		chart: {
			type: 'area',
			width: 130,
			height: 65,
			sparkline: {
				enabled: true
			},
			stacked: true,
			toolbar: {
				show: false
			},
		},
		plotOptions: {
			bar: {
				horizontal: false,
				columnWidth: '25%',
				endingShape: 'rounded'
			},
		},
		legend: {
			position: 'top',
			horizontalAlign: 'left',
			offsetX: 0
		},
		dataLabels: {
			enabled: false
		},
		stroke: {
			show: true,
			width: 2,
			//colors: ['transparent']
		},
		fill: {
			type: 'gradient',
			gradient: {
				shade: 'dark',
				shadeIntensity: 0.15,
				gradientToColors: ['#f02769'],
				type: 'vertical',
				inverseColors: false,
				opacityFrom: 0.1,
				opacityTo: 0.5,
				stops: [0, 50, 65, 91]
			},
		},
		colors: ["#f02769"],
		xaxis: {
			categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月'],
		}
	};
	var chart = new ApexCharts(document.querySelector("#chart2"), options);
	chart.render();
	// chart 3
	var options = {
		series: [{
			name: '订单数',
			data: [10, 9, 24, 19, 22, 9, 12, 7]
		}],
		chart: {
			type: 'bar',
			width: 130,
			height: 65,
			sparkline: {
				enabled: true
			},
			stacked: true,
			toolbar: {
				show: false
			},
		},
		plotOptions: {
			bar: {
				horizontal: false,
				columnWidth: '25%',
				endingShape: 'rounded'
			},
		},
		legend: {
			position: 'top',
			horizontalAlign: 'left',
			offsetX: 0
		},
		dataLabels: {
			enabled: false
		},
		stroke: {
			show: true,
			width: 0,
			colors: ['transparent']
		},
		colors: ["#32ab13"],
		xaxis: {
			categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月'],
		},
		fill: {
			opacity: 1
		}
	};
	var chart = new ApexCharts(document.querySelector("#chart3"), options);
	chart.render();
	// chart 4
	var options = {
		series: [{
			name: '销售额',
			data: [10, 9, 24, 19, 15, 22, 12, 7]
		}],
		chart: {
			type: 'area',
			width: 130,
			height: 65,
			sparkline: {
				enabled: true
			},
			stacked: true,
			toolbar: {
				show: false
			},
		},
		plotOptions: {
			bar: {
				horizontal: false,
				columnWidth: '25%',
				endingShape: 'rounded'
			},
		},
		legend: {
			position: 'top',
			horizontalAlign: 'left',
			offsetX: 0
		},
		dataLabels: {
			enabled: false
		},
		stroke: {
			show: true,
			width: 2,
			//colors: ['transparent']
		},
		fill: {
			type: 'gradient',
			gradient: {
				shade: 'dark',
				shadeIntensity: 0.15,
				gradientToColors: ['#198fed'],
				type: 'vertical',
				inverseColors: false,
				opacityFrom: 0.1,
				opacityTo: 0.5,
				stops: [0, 50, 65, 91]
			},
		},
		colors: ["#198fed"],
		xaxis: {
			categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月'],
		}
	};
	var chart = new ApexCharts(document.querySelector("#chart4"), options);
	chart.render();
	// chart 5
	var options = {
		series: [{
			name: '总金额',
			data: [427, 613, 901, 257, 505, 414, 671, 160, 440]
		}],
		chart: {
			type: 'area',
			height: 80,
			toolbar: {
				show: false
			},
			zoom: {
				enabled: false
			},
			dropShadow: {
				enabled: true,
				top: 3,
				left: 14,
				blur: 4,
				opacity: 0.12,
				color: '#337ab7',
			},
			sparkline: {
				enabled: true
			}
		},
		markers: {
			size: 0,
			colors: ["#337ab7"],
			strokeColors: "#fff",
			strokeWidth: 2,
			hover: {
				size: 7,
			}
		},
		dataLabels: {
			enabled: false
		},
		stroke: {
			show: true,
			width: 3,
			curve: 'smooth'
		},
		colors: ["#337ab7"],
		xaxis: {
			categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
		},
		fill: {
			opacity: 1
		}
	};
	var chart = new ApexCharts(document.querySelector("#chart5"), options);
	chart.render();
	// chart 6
	var options = {
		series: [{
			name: '本月销售',
			data: [427, 613, 901, 257, 505, 414, 671, 160, 440]
		}],
		chart: {
			type: 'area',
			height: 80,
			toolbar: {
				show: false
			},
			zoom: {
				enabled: false
			},
			dropShadow: {
				enabled: true,
				top: 3,
				left: 14,
				blur: 4,
				opacity: 0.12,
				color: '#32ab13',
			},
			sparkline: {
				enabled: true
			}
		},
		markers: {
			size: 0,
			colors: ["#32ab13"],
			strokeColors: "#fff",
			strokeWidth: 2,
			hover: {
				size: 7,
			}
		},
		dataLabels: {
			enabled: false
		},
		stroke: {
			show: true,
			width: 3,
			curve: 'smooth'
		},
		colors: ["#32ab13"],
		xaxis: {
			categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
		},
		fill: {
			opacity: 1
		}
	};
	var chart = new ApexCharts(document.querySelector("#chart6"), options);
	chart.render();
	// chart 7
	var options = {
		series: [{
			name: '本年总销售额',
			data: [427, 613, 901, 257, 505, 414, 671, 160, 440]
		}],
		chart: {
			type: 'area',
			height: 80,
			toolbar: {
				show: false
			},
			zoom: {
				enabled: false
			},
			dropShadow: {
				enabled: true,
				top: 3,
				left: 14,
				blur: 4,
				opacity: 0.12,
				color: '#9900ff',
			},
			sparkline: {
				enabled: true
			}
		},
		markers: {
			size: 0,
			colors: ["#9900ff"],
			strokeColors: "#fff",
			strokeWidth: 2,
			hover: {
				size: 7,
			}
		},
		dataLabels: {
			enabled: false
		},
		stroke: {
			show: true,
			width: 3,
			curve: 'smooth'
		},
		colors: ["#9900ff"],
		xaxis: {
			categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
		},
		fill: {
			opacity: 1
		}
	};
	var chart = new ApexCharts(document.querySelector("#chart7"), options);
	chart.render();
	// chart 8
	var options = {
		series: [25, 45, 10, 10, 10],
		chart: {
			height: 220,
			type: 'donut',
		},
		legend: {
			position: 'bottom',
			show: false,
		},
		plotOptions: {
			pie: {
				// customScale: 0.8,
				donut: {
					size: '70%'
				}
			}
		},
		colors: ["#cc00ff", "#9900ff", "#336666", "#20B2AA", "#333"],
		dataLabels: {
			enabled: false
		},
		labels: ['来源1', '来源2', '来源3', '来源4', '来源5'],
		responsive: [{
			breakpoint: 480,
			options: {
				chart: {
					height: 200
				},
				legend: {
					position: 'bottom'
				}
			}
		}]
	};
	var chart = new ApexCharts(document.querySelector("#chart8"), options);
	chart.render();
	// chart 9
	var options = {
		series: [{
			name: '访客数',
			data: [427, 613, 901, 257, 505, 414, 671, 160, 440]
		}],
		chart: {
			foreColor: '#9ba7b2',
			type: 'bar',
			height: 320,
			toolbar: {
				show: false
			},
			zoom: {
				enabled: false
			},
			dropShadow: {
				enabled: false,
				top: 3,
				left: 10,
				blur: 3,
				opacity: 0.10,
				color: '#337ab7',
			},
			sparkline: {
				enabled: false
			}
		},
		plotOptions: {
			radialBar: {
				hollow: {
					size: '70%',
				}
			},
			bar: {
				horizontal: false,
				columnWidth: '35%',
				endingShape: 'rounded'
			},
		},
		markers: {
			size: 0,
			colors: ["#337ab7"],
			strokeColors: "#fff",
			strokeWidth: 2,
			hover: {
				size: 7,
			}
		},
		dataLabels: {
			enabled: false
		},
		stroke: {
			show: true,
			width: 3,
			curve: 'smooth'
		},
		colors: ["#337ab7"],
		xaxis: {
			categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月'],
		},
		fill: {
			opacity: 1
		}
	};
	var chart = new ApexCharts(document.querySelector("#chart9"), options);
	chart.render();
});