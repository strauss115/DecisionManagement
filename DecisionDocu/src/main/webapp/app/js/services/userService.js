/**
 * service for user information
 */
var userServices = angular.module('userServices', [ 'ngResource' ]);

userServices.factory('UserPerMail', [
		'$resource',
		'$cookies',
		function($resource, $cookies) {
			return $resource(
					serverAddress + '/DecisionDocu/api/web/user/:mail', {
						mail : '@id'
					}, {
						get : {
							method: "GET",
							isArray : false,
							headers : {
								'token' : $cookies['Token']
							}
						}
					});
		} ]);


userServices.factory('UpdateFirstName', [
                             		'$resource',
                             		'$cookies',
                             		function($resource, $cookies) {
                             			return $resource(
                             					serverAddress + '/DecisionDocu/api/web/user/:id/setFirstName?firstName=:firstName', {
                             						id : '@id',
                             						firstName: '@firstName'
           
                             					}, {
                             						save : {
                             							method: "POST",
                             							headers : {
                             								'token' : $cookies['Token']
                             							}
                             						}
                             					});
                             		} ]);
userServices.factory('UpdateLastName', [
                                  		'$resource',
                                  		'$cookies',
                                  		function($resource, $cookies) {
                                  			return $resource(
                                  					serverAddress + '/DecisionDocu/api/web/user/:id/setLastName?lastName=:lastName', {
                                  						id : '@id',
                                  						lastName: '@lastName'
                                  					}, {
                                  						save : {
                                  							method: "POST",
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
			return $resource(serverAddress + '/DecisionDocu/api/upload/profilePicture/:id', {
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
