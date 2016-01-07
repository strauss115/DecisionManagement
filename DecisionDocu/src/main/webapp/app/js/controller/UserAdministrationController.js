/**
 * Controller for the 'userAdministration.html'
 */
app.controller('UserAdministrationController', [ '$scope', 'Users',
		function($scope, Users) {
			$scope.users = Users.query();
		} ]);
/**
 * Method to clear form
 */
$('#userAdministrationClear').click(function() {
	alert("clear");
});

$('#userAdministrationSave').click(function() {

});