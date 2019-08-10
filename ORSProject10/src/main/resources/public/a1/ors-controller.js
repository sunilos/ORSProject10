
/**
 * Login controller
 */

app.controller('loginCtl', function($scope, $location, $http,ServiceLocator) {

	_self = this;

	_self.api = ServiceLocator.endpointService.getAPI(ServiceLocator.endpointService.Login);
	
	/**
	 * HTML Form data
	 */
	$scope.form = {
		error : false, // error
		message : '', // error or sucess message
		data : {
		}, // form data
		inputerror : {} // form input error messages
	};
	
	$scope.submit = function() {
		url=_self.api.endpoint+"/login",
		   $http.post(url, $scope.form.data,function(response) {
					$scope.form.error = !response.success;
					if(response.success){
						$location.url.forward('/');
					}else{
						$scope.form.message = response.result.message;
						$scope.form.inputerror = response.inputerror;
					}
				});
	}
	});

/**
 * ChangePassword controller
 */
app.controller('changePasswordCtl', function($scope, $routeParams, ServiceLocator) {
	
	_self = this;

	initController(_self, ServiceLocator.endpointService.USER, $scope, $routeParams, ServiceLocator);
	
	$scope.submit = function() {
		url=_self.api.endpoint+"/changepassword",
		ServiceLocator.http.post(url, $scope.form.data,
				function(response) {
					$scope.form.error = !response.success;
					if(response.success){
						$scope.form.message = response.result.message;
					}else{
						$scope.form.message = response.result.message;
						$scope.form.inputerror = response.inputerror;
					}
				});
			}
	});

/**
 * Forgot Password controller
 */
app.controller('forgotPasswordCtl', function($scope, $routeParams, ServiceLocator) {

	_self = this;

	initController(_self, ServiceLocator.endpointService.USER, $scope, $routeParams, ServiceLocator);
	
	$scope.submit = function() {
		url=_self.api.endpoint+"/forgetPassword",
		ServiceLocator.http.post(url, $scope.form.data,
				function(response) {
					$scope.form.error = !response.success;
					if(response.success){
						$scope.form.message = response.result.message;
					}else{
						$scope.form.message = response.result.message;
						$scope.form.inputerror = response.inputerror;
					}
				});
			}
});

/**
 * College controller
 */
app.controller('collegeCtl', function($scope, $routeParams, ServiceLocator) {

	_self = this;

	initController(_self, ServiceLocator.endpointService.COLLEGE, $scope, $routeParams, ServiceLocator);

	_self.populateForm  = function(form, data) {
		form.id = data.id;
		form.name = data.name;
		form.address = data.address;
		form.state = data.state;
		form.city = data.city;
		form.phoneNo = data.phoneNo;
		console.log('Populated Form', form, _self.api);
	}

	_self.init();
});

app.controller('collegeListCtl', function($scope, $routeParams, ServiceLocator) {

	_self = this;

	initController(_self, ServiceLocator.endpointService.COLLEGE, $scope, $routeParams, ServiceLocator);

	_self.initList();
});



/**
 * Student controller
 */
app.controller('studentCtl', function($scope, $routeParams, ServiceLocator) {

	_self = this;

	initController(_self, ServiceLocator.endpointService.STUDENT, $scope, $routeParams, ServiceLocator);
	
	_self.populateForm  = function(form, data) {
		form.id = data.id;
		form.firstName = data.firstName;
		form.lastName = data.lastName;
		form.dob = data.dob;
		form.mobileNo = data.mobileNo;
		form.phoneNo = data.phoneNo;
		form.email = data.email;
		form.collegeName = data.collegeName;
		form.collegeId = data.collegeId;
		console.log('Populated Form', form, _self.api);
	}
	
	_self.init();
});

app.controller('studentListCtl', function($scope, $routeParams, ServiceLocator) {

	_self = this;

	initController(_self, ServiceLocator.endpointService.STUDENT, $scope, $routeParams, ServiceLocator);

	_self.initList();
});


/**
 * Marksheet controller
 */
app.controller('marksheetCtl', function($scope, $routeParams, ServiceLocator) {

	_self = this;

	initController(_self, ServiceLocator.endpointService.MARKSHEET, $scope, $routeParams, ServiceLocator);
	
	_self.populateForm  = function(form, data) {
		console.log('data======',data);
		form.id = data.id;
		form.rollNo = data.rollNo;
		form.name = data.name;
		form.physics = data.physics;
		form.chemistry = data.chemistry;
		form.maths = data.maths;
		form.studentId = data.studentId;
		console.log('Populated Form', form, _self.api);
	}
	
	_self.init();
});

app.controller('marksheetListCtl', function($scope, $routeParams, ServiceLocator) {

	_self = this;

	initController(_self, ServiceLocator.endpointService.MARKSHEET, $scope, $routeParams, ServiceLocator);

	_self.initList();
});


/**
 * UserReg controller
 */
app.controller('userRegistrationCtl', function($scope, $routeParams, ServiceLocator) {

	_self = this;

	initController(_self, ServiceLocator.endpointService.USER, $scope, $routeParams, ServiceLocator);
	
	_self.populateForm  = function(form, data) {
		form.id = data.id;
		form.roleId = data.roleId;
		form.firstName = data.firstName;
		form.lastName = data.lastName;
		form.email = data.email;
		form.loginId = data.loginId;
		form.gender = data.gender;
		form.phone = data.phone;
		form.password = data.password;
		form.status = data.status;
	}
	
	_self.init();
});

app.controller('userListCtl', function($scope, $routeParams, ServiceLocator) {

	_self = this;

	initController(_self, ServiceLocator.endpointService.USER, $scope, $routeParams, ServiceLocator);

	_self.initList();
});

/**
 * Role controller
 */
app.controller('roleCtl', function($scope, $routeParams, ServiceLocator) {

	_self = this;
	
	/**
	 * Server API to call
	 */
	_self.api = ServiceLocator.endpointService.getAPI( ServiceLocator.endpointService.Role);

	/**
	 * Initialize single record controller
	 */
	_self.init = function() {
		$scope.form.data.id = 0;
		if ($routeParams.id) {
			$scope.form.data.id = $routeParams.id;
		}
		$scope.display();
		$scope.preload();
	}

	
	/**
	 * Populate bean from response data
	 */
	_self.populateForm  = function(form, data) {
		form.id = data.id;
		form.name = data.name;
		form.discription = data.discription;
		console.log('Populated Form', form, _self.api);
	}

	/**
	 * HTML Form data
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
		pageNo : 0,
		pageSize : 5
	};

	
	/**
	 * Fetches preload data
	 */
	$scope.preload = function() {
		console.log(_self.api.preload);
		ServiceLocator.http.get(_self.api.preload, function(response) {
			$scope.form.preload = response.result;
		});
	}
	
	/**
	 * Fetches a record by primary key. Contains display logic
	 */
	$scope.display = function() {
		if ($scope.form.data.id > 0) {
			var url = _self.api.get + "/" + $scope.form.data.id;
			ServiceLocator.http.get(url, function(response) {
				$scope.form.error = !response.success;
				$scope.form.message = response.result.message;
				_self.populateForm($scope.form.data, response.result.data);
			});
		}
	}	

	/**
	 * Save a record
	 */
	$scope.submit = function() {
		ServiceLocator.http.post(_self.api.save, $scope.form.data,
				function(response) {
					$scope.form.error = !response.success;
					
					if(response.success){
						$scope.form.message = "Data is saved";
					}else{
						$scope.form.message = response.result.message;
						$scope.form.inputerror = response.inputerror;
					}
				});
	}
	

	/**
	 * Navigate to next page
	 */
	$scope.forward = function(page) {
		ServiceLocator.locationService.url(page);
	}


	_self.init();
});

app.controller('roleListCtl', function($scope, $routeParams, ServiceLocator) {

	_self = this;

	/**
	 * Server API to call
	 */
	_self.api = ServiceLocator.endpointService.getAPI( ServiceLocator.endpointService.Role);


	/**
	 * Initialize list controller
	 */
	_self.initList = function() {
		$scope.search();
		$scope.preload();
	}

	/**
	 * Populate bean from response data
	 */
	_self.populateForm  = function(form, data) {
		form.id = data.id;
		form.name = data.name;
		form.discription = data.discription;
		console.log('Populated Form', form, _self.api);
	}
	
	/**
	 * HTML Form data
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
		pageNo : 0,
		pageSize : 5
	};

	
	/**
	 * Fetches preload data
	 */
	$scope.preload = function() {
		console.log(_self.api.preload);
		ServiceLocator.http.get(_self.api.preload, function(response) {
			$scope.form.preload = response.result;
		});
	}		
	

	/**
	 * Deletes a record
	 */

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

	/**
	 * Makes search http call
	 */
	
	$scope.search = function() {
		var url = _self.api.search + "/" + $scope.form.pageNo;
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


	/**
	 * Navigate to next page
	 */
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
	
	_self.initList();
});


/**
 * Message controller
 */
app.controller('messageCtl', function($scope, $routeParams, ServiceLocator) {

	_self = this;

	initController(_self, ServiceLocator.endpointService.Message, $scope, $routeParams, ServiceLocator);
	
	_self.populateForm  = function(form, data) {
		form.id = data.id;
		form.status = data.status;
		form.subject = data.subject;
		form.code = data.code;
		form.type = data.type;
		form.body = data.body;
		console.log('Populated Form', form, _self.api);
	}
	
	_self.init();
});

app.controller('messageListCtl', function($scope, $routeParams, ServiceLocator) {

	_self = this;

	initController(_self, ServiceLocator.endpointService.Message, $scope, $routeParams, ServiceLocator);

	_self.initList();
});


/**
 * Initialize controller
 * 
 * @param ctl
 * @param endpoint
 * @param $scope
 * @param $routeParams
 * @param ServiceLocator
 * @returns
 */
function initController(ctl, endpoint, $scope, $routeParams, ServiceLocator) {

	/**
	 * Server API to call
	 */
	ctl.api = ServiceLocator.endpointService.getAPI(endpoint);

	/**
	 * Initialize single record controller
	 */
	ctl.init = function() {
		$scope.form.data = { };
		$scope.form.data.id = 0;
		$scope.form.message = "";
		$scope.form.error = false;
		
		// Read URI variables /controller/:id
		if ($routeParams.id) {
			$scope.form.data.id = $routeParams.id;
		}
		$scope.display();
		$scope.preload();
	}

	
	/**
	 * Initialize list controller
	 */
	ctl.initList = function() {
		$scope.search();
		$scope.preload();
	}

	/**
	 * Populate bean from response data
	 */
	ctl.populateForm  = function(form, data) {
	}
	
	/**
	 * Check is not null
	 */
	ctl.isNotNullObject = function(val) {
	    var flag = true;
	    try {
	      if (val) {
	        flag = true;
	      } else {
	        flag = false;
	      }
	    } catch (error) {
	      flag = false;
	    }
	    return flag;
	  }
	
	/**
	 * Check is not null and Return Zero
	 */
	ctl.checkNotNullNumber = function(val) {
	    var flag = 0;
	    try {
	      if (val) {
	        flag = val;
	      } else {
	        flag = 0;
	      }
	    } catch (error) {
	      flag = 0;
	    }
	    return flag;
	  }
	

	/**
	 * HTML Form data
	 */
	$scope.form = {
		error : false, // error
		message : '', // error or success message
		preload : null, // preload data
		data : {
			id : null
		}, // form data
		inputerror : {}, // form input error messages
		searchParams : {}, // search form
		searchMessage : null, // search result message
		list : [], // search list
		pageNo : 0,
		pageSize : 5
	};
	
	/**
	 * Fetches a record by primary key
	 */
	$scope.display = function() {
		if ($scope.form.data.id > 0) {
			var url = _self.api.get + "/" + $scope.form.data.id;
			ServiceLocator.http.get(url, function(response) {
				$scope.form.error = !response.success;
				$scope.form.message = response.result.message;
				ctl.populateForm($scope.form.data, response.result.data, response.result );
			});
		}
	}	
	
	$scope.edit = function(id) {
		$scope.form.data.id = id;
		$scope.display();
	}
	
	$scope.add = function() {
		ctl.init();
	}

	// Contains display logic
	
	/**
	 * Fetches preload data
	 */
	$scope.preload = function() {
		console.log(_self.api.preload);
		ServiceLocator.http.get(_self.api.preload, function(response) {
			$scope.form.preload = response.result;
		});
	}	
	
	
	
	/**
	 * Save a record
	 */
	$scope.submit = function() {
		ServiceLocator.http.post(_self.api.save, $scope.form.data,
				function(response) {
					$scope.form.error = !response.success;
					if(response.success){
						$scope.form.message = "Data is saved";
						$scope.search();
						if($scope.form.data.id == 0){
							  $scope.form.data.id = response.result.data; 
						}
					}else{
						$scope.form.message = response.result.message;
						$scope.form.inputerror = response.inputerror;
					}
				});
	}
	
	/**
	 * Deletes a record
	 */

	$scope.delete = function(id) {
		$scope.form.data.id = id;
		if ($scope.form.data.id > 0) {
			var url = _self.api.delete1 + "/" + $scope.form.data.id;
			ServiceLocator.http.get(url, function(response) {
				$scope.form.error = !response.success;
				$scope.form.message = response.result.message;
				$scope.search();					
				//populateForm($scope.form.data, response.result.data);		
			});
		}
	}

	/**
	 * Makes search http call
	 */
	$scope.search = function() {
		var url = _self.api.search + "/" + $scope.form.pageNo;
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


	/**
	 * Navigate to next page
	 */
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

}
