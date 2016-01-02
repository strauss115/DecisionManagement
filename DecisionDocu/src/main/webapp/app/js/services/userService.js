/**
 * service for user information
 */
var userServices = angular.module('userServices', [ 'ngResource' ]);

userServices.factory('Users', [ '$resource', function($resource) {
	return $resource(serverAddress + '/DecisionManagement/rest/user/', {}, {
		query : {
			method : 'GET',
			params : {},
			isArray : true
		}
	});
} ]);

userServices.factory('UserPerMail', [
		'$resource',
		'$cookies',
		function($resource, $cookies) {
			return $resource(
					serverAddress + '/DecisionDocu/api/web/user/:mail', {
						mail : '@id'
					}, {
						get : {
							isArray : false,
							headers : {
								'token' : $cookies['Token']
							}
						}
					});
		} ]);

userServices.factory('UserPicture', [
		'$resource',
		'$cookies',
		function($resource, $cookies) {
			return $resource(serverAddress + '/DecisionDocu/api/web/user/getFile/:id', {
				id : '@id'
			}, {
				get : {
					method: "GET",
					isArray: false,
					headers : {
						'token' : $cookies['Token']
					}
				}
			});
		} ]);
