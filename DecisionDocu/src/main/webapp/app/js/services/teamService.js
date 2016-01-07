/**
 * Services for the TeamAdministrationController
 */
var teamServices = angular.module('teamServices', [ 'ngResource' ]);

/**
 * TeamPerId - Service for getting a team per team id
 */
teamServices.factory('TeamPerId', [
		'$resource',
		'$cookies',
		function($resource, $cookies) {
			return $resource(serverAddress
					+ '/DecisionDocu/api/web/team/:teamId', {
				teamId : '@id'
			}, {
				get : {
					isArray : false,
					headers : {
						'token' : $cookies['Token']
					}
				}
			});
		} ]);

/**
 * Teams - Service for getting all teams
 */
teamServices.factory('Teams', [
		'$resource',
		'$cookies',
		function($resource, $cookies) {
			return $resource(serverAddress + '/DecisionDocu/api/web/team', {},
					{
						get : {
							isArray : true,
							headers : {
								'token' : $cookies['Token']
							}
						}
					});
		} ]);

/**
 * RegisterTeam - Service to register for a team with a key
 */
teamServices
		.factory(
				'RegisterTeam',
				[
						'$resource',
						'$cookies',
						function($resource, $cookies) {
							return $resource(
									serverAddress
											+ '/DecisionDocu/api/web/team/:teamId/addUser?userId=:userId&password=:password',
									{
										teamId : '@teamId',
										userId : '@userId',
										password : "@password"
									}, {
										save : {
											method : 'POST',
											headers : {
												'token' : $cookies['Token']
											}
										}
									});
						} ]);
