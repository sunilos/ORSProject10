var routes = function($routeProvider) {
	$routeProvider.when("/", {
		templateUrl : "Welcome.html"
	}).when("/student", {
		templateUrl : "Student.html",
		controller : 'studentCtl'
	}).when("/student/:id", {
		templateUrl : "Student.html",
		controller : 'studentCtl'

	}).when("/studentList", {
		templateUrl : "StudentList.html",
		controller : 'studentListCtl'
	}).when("/account", {
		templateUrl : "Account.html",
		controller : 'accountCtl'
	}).when("/college", {
		templateUrl : "College.html",
		controller : 'collegeCtl'
	}).when("/college/:id", {
		templateUrl : "College.html",
		controller : 'collegeCtl'
	}).when("/collegeList", {
		templateUrl : "CollegeList.html",
		controller : 'collegeListCtl'
	}).when("/loan", {
		templateUrl : "Loan.html",
		controller : 'loanCtl'
	});
}

// Define routes
app.config(routes);