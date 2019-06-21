/*
 * College controller
 */
var collegeCB = function($scope, $routeParams, ServiceLocator) {

	_self = this;

	var api = null;

	function init() {
		// Receives ID from URL path
		$scope.form.data.id = ($routeParams.id) ? $routeParams.id : 0;
		api = ServiceLocator.endpointService.getAPI(ServiceLocator.endpointService.COLLEGE);
		$scope.display();
	}

	/**
	 * Form contains preload data, error/sucess message
	 */
	$scope.form = {
		error : false, // error
		message : '', // error or sucess message
		preload : null, // preload data
		data : {
			id : null
		}, // form data
		inputerror : {}, // form input error messages
		searchParams : {}, // search form
		searchMessage : null, // search result message
		list : [], // search list
		pageNo : 0
	};

	function populateForm(form, data) {
		form.id = data.id;
		form.name = data.name;
		form.address = data.address;
		form.state = data.state;
		form.city = data.city;
		form.phoneNo = data.phoneNo;
		console.log('Populated Form', form, api);
	}

	// Contains display logic
	$scope.display = function() {
		if ($scope.form.data.id > 0) {
			var url = api.get + "/" + $scope.form.data.id;
			ServiceLocator.http.get(url, function(response) {
				$scope.form.error = !response.success;
				$scope.form.message = response.result.message;
				populateForm($scope.form.data, response.result.data);
			});
		}
	}

	// Contains submit logic
	$scope.submit = function() {
		ServiceLocator.http.post(api.save, $scope.form.data,
				function(response) {
					$scope.form.error = !response.success;
					$scope.form.message = response.result.message;
					$scope.form.inputerror = response.inputerror;
				});
	}

	$scope.search = function() {
		var url = api.search + "/" + $scope.form.pageNo;
		ServiceLocator.http.post(url, $scope.form.searchParams, function(
				response) {
			$scope.form.error = !response.success;
			$scope.form.searchMessage = response.result.message;
			$scope.form.list = response.result.data;
			if ($scope.form.list.length == 0) {
				$scope.form.searchMessage = "No record found";
			}
		});
	}

	$scope.delete = function() {
		if ($scope.form.data.id > 0) {
			var url = api.delete + "/" + $scope.form.data.id;
			ServiceLocator.http.get(url, function(response) {
				$scope.form.error = !response.success;
				$scope.form.message = response.result.message;
				populateForm($scope.form.data, response.result.data);
			});
		}
	}

	// Upload File
	$scope.upload = function() {
		console.log('upload');
		var url = "api/College/upload";
		ServiceLocator.http.postMutipart(url, $scope.folder, $scope.form,
				$scope.processResponse);
	}

	$scope.forward = function(page) {
		ServiceLocator.locationService.url(page);
	}

	$scope.next = function() {
		$scope.form.pageNo++;
		$scope.search();
	}

	$scope.previous = function() {
		if ($scope.form.pageNo > 0) {
			$scope.form.pageNo--;
			$scope.search();
		}
	}

	init();
};

function initController(ctl, endpoint, $scope, $routeParams, ServiceLocator) {

	ctl.api = ServiceLocator.endpointService.getAPI(endpoint);

	ctl.init = function() {
		$scope.form.data.id = 0;
		// Receives ID from URL path
		if ($routeParams.id) {
			$scope.form.data.id = $routeParams.id;
		}
		$scope.display();
	}

}

// Add callback method to controller
app.controller('collegeCtl', collegeCB);