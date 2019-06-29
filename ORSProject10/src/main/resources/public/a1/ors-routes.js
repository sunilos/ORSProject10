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
			
	}).when("/marksheet", {
		templateUrl : "Marksheet.html",
		controller : 'marksheetCtl'
			
	}).when("/marksheet/:id", {
		templateUrl : "Marksheet.html",
		controller : 'marksheetCtl'

	}).when("/marksheetList", {
		templateUrl : "MarksheetList.html",
		controller : 'marksheetListCtl'
			
	}).when("/userRegistration", {
		templateUrl : "UserRegistration.html",
		controller : 'userRegistrationCtl'
			
	}).when("/userRegistration/:id", {
		templateUrl : "UserRegistration.html",
		controller : 'userRegistrationCtl'
			
	}).when("/user", {
		templateUrl : "User.html",
		controller : 'userRegistrationCtl'
			
	}).when("/user/:id", {
		templateUrl : "User.html",
		controller : 'userRegistrationCtl'
			
	}).when("/userList", {
		templateUrl : "UserList.html",
		controller : 'userListCtl'
			
	}).when("/roleList", {
		templateUrl : "RoleList.html",
		controller : 'roleListCtl'
			
	}).when("/role", {
		templateUrl : "Role.html",
		controller : 'roleCtl'
			
	}).when("/role/:id", {
		templateUrl : "role.html",
		controller : 'roleCtl'

	}).when("/message", {
		templateUrl : "Message.html",
		controller : 'messageCtl'
			
	}).when("/message/:id", {
		templateUrl : "Message.html",
		controller : 'messageCtl'

	}).when("/messageList", {
		templateUrl : "MessageList.html",
		controller : 'messageListCtl'
			
	}).when("/login", {
		templateUrl : "Login.html",
		controller : 'loginCtl'
			
	}).when("/changePassword", {
		templateUrl : "ChangePassword.html",
		controller : 'changePasswordCtl'
			
	}).when("/forgotPassword", {
		templateUrl : "ForgotPassword.html",
		controller : 'forgotPasswordCtl'

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