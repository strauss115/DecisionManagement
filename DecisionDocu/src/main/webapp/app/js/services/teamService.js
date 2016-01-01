/**
 * service for team information
 */
var teamServices = angular.module('teamServices', ['ngResource']);

teamServices.factory('TeamPerId', ['$resource', '$cookies',
                                     function ($resource, $cookies) {
                                         return $resource(serverAddress + '/DecisionDocu/api/web/team/:teamId', {teamId:'@id'} , {
                                         	 get: {
                                         		 	isArray: false,
                                         	        headers: {
                                         	            'token': $cookies['Token']
                                         	        }
                                         	    }
                                         });
                                     }]);


teamServices.factory('Teams', ['$resource', '$cookies',
                                   function ($resource, $cookies) {
                                       return $resource(serverAddress + '/DecisionDocu/api/web/team', {} , {
                                       	 get: {
                                       		 	isArray: true,
                                       	        headers: {
                                       	            'token': $cookies['Token']
                                       	        }
                                       	    }
                                       });
                                   }]);

teamServices.factory('RegisterTeam', ['$resource', '$cookies',
                               function ($resource, $cookies) {
                                   return $resource(serverAddress + '/DecisionDocu/api/web/team/:teamId/addUser?userId=:userId&password=:password', {teamId:'@teamId',userId : '@userId', password: "@password"} , {
                                   	 save: {
                                   		 	method: 'POST',
                                   	        headers: {
                                   	            'token': $cookies['Token']
                                   	        }
                                   	    }
                                   });
                               }]);



