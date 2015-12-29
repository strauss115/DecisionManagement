/**
 * service for the authentification
 */
var decisionServices = angular.module('decisionServices', ['ngResource', 'ngCookies']);


decisionServices.factory('LoadEditGraph', ['$resource', '$cookies',
    function ($resource, $cookies) {
        return $resource(serverAddress + '/DecisionDocu/api/web/decision/getGraphAsJsonById', {}, {
        	 get: {
        		 	isArray: true,
        	        headers: {
        	            'token': $cookies['Token']
        	        }
        	    }
        });
    }]);

decisionServices.factory('LoadConnectionsGraph', ['$resource', '$cookies',
                                           function ($resource, $cookies) {
                                               return $resource(serverAddress + '/DecisionDocu/api/web/decision/getTeamGraphsForConnectionAsJsonByTeamId', {}, {
                                               	 get: {
                                               	        headers: {
                                               	            'token': $cookies['Token']
                                               	        }
                                               	    }
                                               });
                                           }]);
decisionServices.factory('DecisionsByTeam', ['$resource', '$cookies',
                                                  function ($resource, $cookies) {
                                                      return $resource(serverAddress + '/DecisionDocu/api/web/decision/byTeam/' + $cookies['TeamId'], {}, {
                                                      	 get: {
                                                      		 	isArray: true,
                                                      	        headers: {
                                                      	            'token': $cookies['Token']
                                                      	        }
                                                      	    }
                                                      });
                                                  }]);
