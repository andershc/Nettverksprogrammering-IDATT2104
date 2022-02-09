#include <iostream>
#include <thread>
#include <vector>
#include <list>
#include <mutex>

using namespace std;

bool isPrime(int n){
    if (n == 0 || n == 1) {
        return false;
    }
    else {
        for (int j = 2; j <= n / 2; j++) {
            if (n % j == 0) {
                return false;
            }
        }
    }
    return true;
}

int main() {
    int startNumber = 0;
    int endNumber = 1000;
    int numberOfThreads = 5;
    int currentIndex;
    int blocksize = (endNumber-startNumber) / numberOfThreads;

    std::mutex mtx;

    list <int> primeNumbers;
    vector<thread> threads;

    for (int i = 0; i < numberOfThreads; i++) {
        if(currentIndex > endNumber){
            break;
        }
        threads.emplace_back([i, &startNumber, &primeNumbers, endNumber, &mtx, &currentIndex, &blocksize] {

            for(int n = startNumber + i * blocksize; n < startNumber + (i+1) * blocksize; n++) {
                currentIndex = n;
                if(currentIndex > endNumber){
                    break;
                }
                if(isPrime(n)){
                    mtx.lock();
                    primeNumbers.push_back(n);
                    mtx.unlock();
                }
            }
        });
    }

    for (auto &thread : threads) {
        thread.join();
    }

    primeNumbers.sort();
    for (auto i : primeNumbers){
        std::cout << i << " ";
    }

    cout << "\n\nAmount of prime numbers: " << primeNumbers.size();

}


