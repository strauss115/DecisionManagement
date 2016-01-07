/**
 * Services for the LoginController
 */
var loginServices = angular.module('loginServices', [ 'ngResource' ]);

/**
 * Login - Service for the login process
 */
loginServices.factory('Login', [ '$resource', function($resource) {
	return $resource(serverAddress + '/DecisionDocu/api/user/login', {}, {});
} ]);

/**
 * Register - Service for the registration process
 */
loginServices.factory('Register', [
		'$resource',
		function($resource) {
			return $resource(serverAddress + '/DecisionDocu/api/user/register',
					{}, {});
		} ]);
