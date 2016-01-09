/**
 * Services for the CreateDecisionController, EditDecisionController and
 * CompleteDecisionController
 */
var decisionServices = angular.module('decisionServices', [ 'ngResource',
		'ngCookies' ]);

/**
 * LoadEditGraph - Service generates the JSON-model for the decision go.js
 * graph.
 */
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

/**
 * LoadConnectionsGraph - Service generates the JSON-model for the connection
 * go.js graph.
 */
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
/**
 * DecisionsByTeam - Service for getting all decision of a team
 */
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

/**
 * AddAttributeToDecision - Service for adding an attribute to a decison
 */
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

/**
 * CreateDecision Service - for creating a new decision
 */
decisionServices
		.factory(
				'CreateDecision',
				[
						'$resource',
						'$cookies',
						function($resource, $cookies) {
							return $resource(
									serverAddress
											+ '/DecisionDocu/api/web/decision/create?name=:name&userEmail='
											+ $cookies['Mail'] + '&teamId='
											+ $cookies['TeamId'], {
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

/**
 * GetNode - Service to get data of a node element in the decision graph
 */
decisionServices.factory('GetNode', [ '$resource', '$cookies',
		function($resource, $cookies) {
			return $resource(serverAddress + '/DecisionDocu/api/node/:id', {
				id : '@id'
			}, {
				get : {
					method : 'GET',
					isArray : false,
					headers : {
						'token' : $cookies['Token']
					}
				}
			});
		} ]);

/**
 * GetFile - Service for getting a file in base64 format
 */
userServices.factory('GetFile', [
		'$resource',
		'$cookies',
		function($resource, $cookies) {
			return $resource(serverAddress
					+ '/DecisionDocu/api/upload/document/:id', {
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

/**
 * GetMessages - Service for getting all messages related to a decision
 */
decisionServices.factory('GetMessages', [
		'$resource',
		'$cookies',
		function($resource, $cookies) {
			return $resource(serverAddress
					+ '/DecisionDocu/api/web/decision/:id/messages', {
				id : '@id'
			}, {
				get : {
					method : 'GET',
					isArray : true,
					headers : {
						'token' : $cookies['Token']
					}
				}
			});
		} ]);