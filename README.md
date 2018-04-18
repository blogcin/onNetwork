# OnNetwork

Java based socket broadcast library

## How to use?

Add it in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
	
Add the dependency
```
dependencies {
    compile 'com.github.blogcin:OnNetwork:+'
}
```

## Example Code
```
OnNetwork onNetwork = OnNetwork.generate();
onNetwork.receiveBroadCastStart(new OnNetworkImpl() {
   @Override
   public void onBroadCastReceived(byte[] bytes) {
       System.out.println("Receievd : "+ new String(bytes));
   }
});

onNetwork.sendBroadCast(NetworkProp.getInstance().getAdaptersAddress(),
      "test_nickname", "test_message");

try {
   Thread.sleep(5000);
} catch (InterruptedException e) {
   e.printStackTrace();
}
        
onNetwork.receiveBroadCastStop();
```
