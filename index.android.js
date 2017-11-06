'use strict';

const ReactNative = require('react-native')
const RNPermissions = ReactNative.NativeModules.ReactNativePermissions;
const AsyncStorage = ReactNative.AsyncStorage


const RNPTypes = [
	'location',
	'camera',
	'microphone',
	'photo',
	'contacts',
	'event',
	'callPhone',
	'readSms',
	'receiveSms',
]

class ReactNativePermissions {
	canOpenSettings() {
		return false
	}

	openSettings() {
		return Promise.reject('\'openSettings\' is Depricated on android')
	}

	getTypes() {
		return Object.keys(RNPTypes);
	}

	check(permission, type) {
  	if (!RNPTypes.includes(permission)) {
			return Promise.reject(`ReactNativePermissions: ${permission} is not a valid permission type on iOS`);
		}

		console.log('** permission=', permission);

		return RNPermissions.getPermissionStatus(permission, type);
	}

	 checkGPSPermision(){
		 return RNPermissions.checkGPSPermision()
	 }

	request(permission, type) {


		if (!RNPTypes.includes(permission)) {
			return Promise.reject(`ReactNativePermissions: ${permission} is not a valid permission type on iOS`);
		}

		if (permission == 'backgroundRefresh') {
			return Promise.reject('ReactNativePermissions: You cannot request backgroundRefresh')
		}

		console.log('** request=', permission);

		return RNPermissions.requestPermission(permission, type)
	}

	checkMultiple(permissions) {
		return Promise.all(permissions.map(this.check.bind(this)))
			.then(res => res.reduce((pre, cur, i) => {
				var name = permissions[i]
				pre[name] = cur
				return pre
			}, {}))
	}
}

module.exports = new ReactNativePermissions()
