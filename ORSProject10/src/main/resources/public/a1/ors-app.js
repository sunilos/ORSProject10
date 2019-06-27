var app = angular.module("myApp", [ "ngRoute" ]);

/**
 * File upload directive
 */
app.directive('fileModel', [ '$parse', function($parse) {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			var model = $parse(attrs.fileModel);
			var modelSetter = model.assign;
			element.bind('change', function() {
				scope.$apply(function() {
					modelSetter(scope, element[0].files[0]);
				});
			});
		}
	};
} ]);

// Service
app.service('httpCallService', function($http, ErrorService) {

	var self = this;

	self.get = function(url, ctlResponse) {
		$http.get(url).then(
				function success(serverResponse) {
					ctlResponse(serverResponse.data);
				},
				function fail(serverResponse) {
					console.log(serverResponse);
					ErrorService.setMessage(serverResponse.status,
							serverResponse.statusText);
				});
	}

	self.post = function(url, data, ctlResponse) {
		$http.post(url, data).then(
				function(serverResponse) {
					ctlResponse(serverResponse.data);
				},
				function(serverResponse) {
					console.log(serverResponse);
					ErrorService.setMessage(serverResponse.status,
							serverResponse.statusText);
				});
	}

	self.postMutipart = function(url, folder, data, ctlResponse) {
		var fd = new FormData();
		fd.append('file', folder.file);
		for ( var key in data) {
			if (data.hasOwnProperty(key)) {
				fd.append(key, data[key]);
			}
		}

		fd.append("form", angular.toJson(data));

		$http.post(url, fd, {
			withCredentials : false,
			transformRequest : angular.identity,
			headers : {
				'Content-Type' : undefined
			},
			data : angular.toJson(data)
		}).then(
				function(serverResponse) {
					ctlResponse(serverResponse.data);
				},
				function(serverResponse) {
					console.log(serverResponse);
					ErrorService.setMessage(serverResponse.status,
							serverResponse.statusText);
				});
	};
});

/**
 * ServiceLocator locates singleton services of application
 */
app.service('ServiceLocator',
		function(httpCallService, $location, EndpointService, ErrorService) {
			var self = this;
			self.http = httpCallService;
			self.locationService = $location;
			self.errorService = ErrorService;
			self.endpointService = EndpointService;
		});

/**
 * Service contains endpoints
 */
app.service('EndpointService', function() {
	var self = this;
	this.COLLEGE = "/College";
	this.STUDENT = "/Student";
	this.MARKSHEET = "/Marksheet";
	this.USER = "/User";
	this.Role = "/Role";
	this.Message = "/Message";
	this.Login = "/Auth";
	
	self.getAPI = function(ep){
		return {
			endpoint : ep,
			get : ep +"/get",
			save : ep +"/save",
			delete1 : ep +"/delete",
			preload : ep + "/preload",
			search : ep + "/search"
		}
	}
});

/**
 * Service process server exceptions
 */
app.service('ErrorService', function() {
	self = this;
	self.code = 0;
	self.setCode = function(c) {
		self.code = c;
	};
	self.getCode = function(c) {
		return self.code;
	};
	self.setMessage = function(c, m) {
		self.code = c;
		self.message = m;
	};
	self.getMessage = function() {
		return self.code + ":" + self.message;
	};
});

/**
 * Error controller
 */
var errorCB = function($scope, ServiceLocator) {
	$scope.errorMsg = "";
	$scope.errorShow = false;

	$scope.reset = function() {
		ServiceLocator.errorService.setCode(0);
		$scope.errorShow = false;
	}

	$scope.$watch(function() {
		return ServiceLocator.errorService.getCode();
	}, function(newVal, oldVal) {
		if (newVal > 0) {
			$scope.errorMsg = ServiceLocator.errorService.getMessage();
			$scope.errorShow = true;
		}
	});
}
app.controller('errorCtl', errorCB);
