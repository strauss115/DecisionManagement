/**
 * service for the authentification
 */
var decisionServices = angular.module('decisionServices', [ 'ngResource',
		'ngCookies' ]);

decisionServices.factory('LoadEditGraph', [
		'$resource',
		'$cookies',
		function($resource, $cookies) {
			return $resource(serverAddress
					+ '/DecisionDocu/api/web/decision/getGraphAsJsonById', {},
					{
						get : {
							isArray : true,
							headers : {
								'token' : $cookies['Token']
							}
						}
					});
		} ]);

decisionServices
		.factory(
				'LoadConnectionsGraph',
				[
						'$resource',
						'$cookies',
						function($resource, $cookies) {
							return $resource(
									serverAddress
											+ '/DecisionDocu/api/web/decision/getTeamGraphsForConnectionAsJsonByTeamId',
									{}, {
										get : {
											headers : {
												'token' : $cookies['Token']
											}
										}
									});
						} ]);
decisionServices.factory('DecisionsByTeam', [
		'$resource',
		'$cookies',
		function($resource, $cookies) {
			return $resource(serverAddress
					+ '/DecisionDocu/api/web/decision/byTeam/'
					+ $cookies['TeamId'], {}, {
				get : {
					isArray : true,
					headers : {
						'token' : $cookies['Token']
					}
				}
			});
		} ]);
decisionServices
		.factory(
				'AddAttributeToDecision',
				[
						'$resource',
						'$cookies',
						function($resource, $cookies) {
							return $resource(
									serverAddress
											+ '/DecisionDocu/api/web/decision/:decisionId/:attribute?value=:value',
									{
										decisionId : '@id',
										attribute : '@attribute',
										value : '@value'
									}, {
										save : {
											method : 'POST',
											headers : {
												'token' : $cookies['Token']
											}
										}
									});
						} ]);
decisionServices
		.factory(
				'CreateDecision',
				[
						'$resource',
						'$cookies',
						function($resource, $cookies) {
							return $resource(
									serverAddress
											+ '/DecisionDocu/api/web/decision/create?name=:name&userEmail=' + $cookies['Mail'] + '&teamId='
											+ $cookies['TeamId'],
									{
										name : '@decisionName'
									}, {
										save : {
											method : 'POST',
											headers : {
												'token' : $cookies['Token']
											}
										}
									});
						} ]);


decisionServices
.factory(
		'GetNode',
		[
				'$resource',
				'$cookies',
				function($resource, $cookies) {
					return $resource(
							serverAddress
									+ '/DecisionDocu/api/node/:id',
							{
								id : '@id'
							}, {
								get : {
									method : 'GET',
									isArray: false,
									headers : {
										'token' : $cookies['Token']
									}
								}
							});
				} ]);

userServices.factory('GetFile', [
                             		'$resource',
                             		'$cookies',
                             		function($resource, $cookies) {
                             			return $resource(serverAddress + '/DecisionDocu/api/upload/document/:id', {
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