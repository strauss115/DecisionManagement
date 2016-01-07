/**
 * Services for the UserProfileController
 */
var userServices = angular.module('userServices', [ 'ngResource' ]);

/**
 * UserPerMail - Service for getting a user per team mail adress
 */
userServices.factory('UserPerMail', [
		'$resource',
		'$cookies',
		function($resource, $cookies) {
			return $resource(
					serverAddress + '/DecisionDocu/api/web/user/:mail', {
						mail : '@id'
					}, {
						get : {
							method : "GET",
							isArray : false,
							headers : {
								'token' : $cookies['Token']
							}
						}
					});
		} ]);

/**
 * UpdateFirstName - Service for updating the firstname of a user
 */
userServices
		.factory(
				'UpdateFirstName',
				[
						'$resource',
						'$cookies',
						function($resource, $cookies) {
							return $resource(
									serverAddress
											+ '/DecisionDocu/api/web/user/:id/setFirstName?firstName=:firstName',
									{
										id : '@id',
										firstName : '@firstName'

									}, {
										save : {
											method : "POST",
											headers : {
												'token' : $cookies['Token']
											}
										}
									});
						} ]);

/**
 * UpdateLastName - Service for updating the lastname of a user
 */
userServices
		.factory(
				'UpdateLastName',
				[
						'$resource',
						'$cookies',
						function($resource, $cookies) {
							return $resource(
									serverAddress
											+ '/DecisionDocu/api/web/user/:id/setLastName?lastName=:lastName',
									{
										id : '@id',
										lastName : '@lastName'
									}, {
										save : {
											method : "POST",
											headers : {
												'token' : $cookies['Token']
											}
										}
									});
						} ]);

/**
 * UserPicture - Service for getting the profile picture
 */
userServices.factory('UserPicture', [
		'$resource',
		'$cookies',
		function($resource, $cookies) {
			return $resource(serverAddress
					+ '/DecisionDocu/api/upload/profilePicture/:id', {
				id : '@id'
			}, {
				get : {
					method : "GET",
					isArray : false,
					headers : {
						'token' : $cookies['Token']
					}
				}
			});
		} ]);
